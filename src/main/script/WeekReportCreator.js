const {readFileSync, existsSync, unlinkSync} = require("node:fs");
const {join} = require("node:path");

/**
 * 根据日期获取对应的文件路径
 * @param {Date} date - 日期对象
 * @param {string} vaultPath - Obsidian Vault的路径
 * @returns {string} 完整的文件路径
 */
function getFilePathByDate(date, vaultPath) {
    try {
        if (!date || !(date instanceof Date)) {
            throw new Error('无效的日期对象');
        }

        if (!vaultPath || typeof vaultPath !== 'string') {
            throw new Error('无效的Vault路径');
        }

        const year = date.getFullYear();
        const month = date.getMonth() + 1; // getMonth() 返回 0-11
        const day = String(date.getDate()).padStart(2, '0');

        // 构建文件路径：Vault路径/日报/年份/月份/文件名
        const monthPath = join(vaultPath, `日报/${year}年/${month}月/`);
        const fileName = `${year}-${String(month).padStart(2, '0')}-${day}.md`;

        return join(monthPath, fileName);
    } catch (error) {
        throw new Error(`构建文件路径失败: ${error.message}`);
    }
}

/**
 * 获取当前周周一至周五的日期数组和文件名数组
 * @param {string} vaultPath - Obsidian Vault的路径
 * @returns {Array} 包含日期对象和文件名的数组
 */
function getCurrentWeekDayInfo(vaultPath) {
    try {
        const now = new Date();
        const day = now.getDay() || 7; // 调整周日为7
        const diff = now.getDate() - day + 1; // 计算周一的日期差

        const result = [];

        // 生成周一至周五的日期信息
        for (let i = 0; i < 5; i++) {
            const date = new Date(now);
            date.setDate(diff + i);

            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            const fileName = `${year}-${month}-${day}.md`;

            result.push({
                date: date,
                fileName: fileName,
                fullPath: getFilePathByDate(date, vaultPath)
            });
        }

        return result;
    } catch (error) {
        throw new Error(`获取本周工作日信息失败: ${error.message}`);
    }
}

/**
 * 检测文件是否包含实际工作内容
 * @param {string} filePath - 文件路径
 * @returns {boolean} 如果文件包含工作内容返回true，否则返回false
 */
function hasWorkContent(filePath) {
    try {
        if (!existsSync(filePath)) {
            return false;
        }

        const content = readFileSync(filePath, "utf-8");
        const lines = content.split('\n');

        let hasContent = false;
        let isInWorkSection = false;

        for (let i = 0; i < lines.length; i++) {
            const line = lines[i].trim();

            // 检查是否是工作类别标题
            if (line.startsWith('### 新增') || line.startsWith('### 优化') || line.startsWith('### 修正')) {
                isInWorkSection = true;
                continue;
            }

            // 检查是否是其他标题或空行
            if (line.startsWith('### ') || line === '') {
                isInWorkSection = false;
                continue;
            }

            // 在工作区域内检查是否有实际内容（排除注释和空行）
            if (isInWorkSection && line && !line.startsWith('<!--') && !line.endsWith('-->')) {
                // 检查是否是带有序号的内容行（如：1. 内容）
                if (line.match(/^\s*\d+\.\s+/)) {
                    hasContent = true;
                    break;
                }
                // 检查是否是其他非空内容
                if (line.length > 2 && !line.startsWith('//') && !line.startsWith('#')) {
                    hasContent = true;
                    break;
                }
            }
        }

        return hasContent;
    } catch (error) {
        console.error(`检测文件内容时出错: ${filePath}`, error);
        return false;
    }
}

/**
 * 删除没有工作内容的日报文件
 * @param {string} vaultPath - Obsidian Vault的路径
 * @param {boolean} debug - 是否输出调试信息
 * @returns {Object} 包含删除结果的对象
 */
function deleteEmptyDailyFiles(vaultPath, debug = false) {
    try {
        const now = new Date();
        const weekDayInfo = getCurrentWeekDayInfo(vaultPath);
        const deletedFiles = [];
        const keptFiles = [];

        weekDayInfo.forEach(info => {
            if (existsSync(info.fullPath)) {
                if (hasWorkContent(info.fullPath)) {
                    keptFiles.push(info.fileName);
                    if (debug) {
                        console.log(`保留文件: ${info.fileName} - 包含工作内容`);
                    }
                } else {
                    try {
                        unlinkSync(info.fullPath);
                        deletedFiles.push(info.fileName);
                        if (debug) {
                            console.log(`删除文件: ${info.fileName} - 无工作内容`);
                        }
                    } catch (error) {
                        console.error(`删除文件失败: ${info.fileName}`, error);
                    }
                }
            } else {
                if (debug) {
                    console.log(`文件不存在: ${info.fileName}`);
                }
            }
        });

        return {
            deleted: deletedFiles,
            kept: keptFiles,
            totalChecked: weekDayInfo.length
        };
    } catch (error) {
        console.error('删除空日报文件时出错:', error);
        return {
            deleted: [],
            kept: [],
            totalChecked: 0,
            error: error.message
        };
    }
}

/**
 * 提取指定范围内的工作内容，并在合并时只保留一个标题，同时重新排序序号
 * @param {string} workContent - 所有工作日志内容
 * @param {string} retrieveScope - 提取范围（新增/优化/修正）
 * @returns {string} 提取后的内容，只保留一个标题并重新排序序号
 */
function retrieveWorkContent(workContent, retrieveScope) {
    try {
        // 空值检查
        if (!workContent || typeof workContent !== 'string') {
            return `<!-- 警告：工作内容为空或格式不正确 -->
### ${retrieveScope}
暂无内容`;
        }

        // 直接使用字符串处理，避免复杂正则表达式的问题
        const lines = workContent.split('\n');
        let allItems = [];
        let isInTargetSection = false;

        // 逐行处理
        for (let i = 0; i < lines.length; i++) {
            const line = lines[i].trim();

            // 检查是否是目标类别标题
            const isTargetHeading = line.startsWith(`### ${retrieveScope}`);
            // 检查是否是其他类别标题
            const isOtherHeading = line.startsWith('### ') && !isTargetHeading;

            if (isTargetHeading) {
                // 进入目标类别区域
                isInTargetSection = true;
            } else if (isOtherHeading || i === lines.length - 1) {
                // 离开目标类别区域或到达文件末尾
                if (isInTargetSection && i === lines.length - 1 && line) {
                    allItems.push(line);
                }
                isInTargetSection = false;
            } else if (isInTargetSection && line) {
                // 在目标类别区域内且不是空行，添加到结果中
                allItems.push(line);
            }
        }

        // 如果没有内容项，返回空内容提示
        if (allItems.length === 0) {
            return `### ${retrieveScope}\n<!--暂无内容-->`;
        }

        // 重新排序序号
        const processedItems = [];
        let counter = 1;

        allItems.forEach(item => {
            // 检查是否是带有序号的行（如：1. 内容）
            const numMatch = item.match(/^\s*(\d+)\.\s+/);
            if (numMatch) {
                // 提取内容部分并添加新序号
                const content = item.substring(numMatch[0].length);
                processedItems.push(`${counter}. ${content}`);
                counter++;
            } else {
                // 非序号行直接保留
                processedItems.push(item);
            }
        });

        // 只保留一个标题，并合并所有内容项
        let result = `### ${retrieveScope}\n`;
        result += processedItems.join('\n');

        return result;
    } catch (error) {
        // 捕获retrieveWorkContent中的错误
        return `<!-- 提取${retrieveScope}内容时出错 -->\n### ${retrieveScope}\n[错误]: ${error.message}`;
    }
}

/**
 * 构建当前周的工作内容
 * @param {string} vaultPath - Obsidian Vault的路径
 * @returns {Object} 包含工作内容和错误信息的对象
 */
function buildCurWeekContent(vaultPath) {
    try {
        const weekDayInfo = getCurrentWeekDayInfo(vaultPath);
        let workContent = "";
        let errorMessages = [];

        weekDayInfo.forEach(info => {
            try {
                const fileContent = readFileSync(info.fullPath, "utf-8");
                workContent += fileContent + "\n";
            } catch (error) {
                const errorMsg = `文件不存在或无法读取: ${info.fileName} (路径: ${info.fullPath})`;
                errorMessages.push(errorMsg);
                console.warn(errorMsg);
            }
        });

        return {
            content: workContent,
            errors: errorMessages
        };
    } catch (error) {
        throw new Error(`构建本周工作内容失败: ${error.message}`);
    }
}

/**
 * 构建周报内容，并可选删除空文件
 * @param {string} vaultPath - Obsidian Vault的路径（可选，如果不提供将尝试自动获取）
 * @param {boolean} debug - 是否返回日志信息，默认为false
 * @param {boolean} deleteEmpty - 是否删除没有工作内容的文件，默认为false
 * @returns {string} 生成的周报内容
 */
function buildWeekReport(vaultPath, debug = false, deleteEmpty = false) {
    try {
        // 记录开始时间
        const startTime = new Date();

        // 根据debug参数决定是否包含执行开始时间的注释
        let result = debug ? `<!-- 周报生成于 ${startTime.toLocaleString()} -->\n` : '';

        // 检查是否提供了vaultPath
        if (!vaultPath) {
            throw new Error('未提供Vault路径，请通过Templater传递tp.vault.path');
        }

        // 根据debug参数决定是否包含Vault路径信息
        if (debug) {
            result += `<!-- 使用Vault路径: ${vaultPath} -->\n\n`;
        }

        // 如果启用了删除空文件功能
        if (deleteEmpty) {
            const deleteResult = deleteEmptyDailyFiles(vaultPath, debug);
            if (debug) {
                result += `<!-- 空文件清理结果: 检查了${deleteResult.totalChecked}个文件，删除了${deleteResult.deleted.length}个空文件，保留了${deleteResult.kept.length}个有内容的文件 -->\n\n`;
                if (deleteResult.deleted.length > 0) {
                    result += `<!-- 删除的文件: ${deleteResult.deleted.join(', ')} -->\n\n`;
                }
                if (deleteResult.error) {
                    result += `<!-- 清理过程中出现错误: ${deleteResult.error} -->\n\n`;
                }
            }
        }

        // 构建本周工作内容
        const weekContentResult = buildCurWeekContent(vaultPath);

        // 根据debug参数决定是否包含文件读取错误信息
        if (debug && weekContentResult.errors && weekContentResult.errors.length > 0) {
            result += `<!-- 以下文件无法读取: \n${weekContentResult.errors.join('\n')}\n-->` + "\n\n";
        }

        // 提取各类别内容
        const addContent = retrieveWorkContent(weekContentResult.content, "新增");
        const improveContent = retrieveWorkContent(weekContentResult.content, "优化");
        const fixContent = retrieveWorkContent(weekContentResult.content, "修正");

        // 组合最终结果
        result += addContent + "\n\n" + improveContent + "\n\n" + fixContent;

        // 根据debug参数决定是否包含执行结束时间和耗时
        if (debug) {
            const endTime = new Date();
            result += `\n\n<!-- 周报生成完成于 ${endTime.toLocaleString()}，耗时 ${endTime - startTime}ms -->`;
        }

        return result;
    } catch (error) {
        // 捕获并返回所有错误
        const errorMessage = `# 【错误】周报生成失败\n\n
        ## 错误详情\n- 错误信息: ${error.message}\n- 当前使用的Vault路径: ${vaultPath || '未提供'}\n\n
        ## 请检查\n1. Vault路径是否正确\n2. 日报文件格式是否符合要求\n3. 脚本是否有权限读取文件`;
        console.error(errorMessage);
        return errorMessage;
    }
}

/**
 * 周报提取脚本
 * @author tomato
 * @date 2025-09-28
 */
module.exports = buildWeekReport;
module.exports.deleteEmptyDailyFiles = deleteEmptyDailyFiles;
module.exports.hasWorkContent = hasWorkContent;
