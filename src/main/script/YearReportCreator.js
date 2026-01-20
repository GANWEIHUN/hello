const {readFileSync, existsSync, writeFileSync, readdirSync, statSync} = require("node:fs");
const {join, extname, basename} = require("node:path");

/**
 * 获取指定年份的所有周报文件
 * @param {string} vaultPath - 周报所在的目录路径
 * @param {number} year - 年份（可选，如果不提供则从路径中提取）
 * @returns {Array} 包含文件信息的数组
 */
function getAllWeeklyReports(vaultPath, year = null) {
    try {
        if (!existsSync(vaultPath)) {
            throw new Error(`目录不存在: ${vaultPath}`);
        }

        // 如果没有提供年份，尝试从路径中提取
        if (!year) {
            const yearMatch = vaultPath.match(/(\d{4})年/);
            if (yearMatch) {
                year = parseInt(yearMatch[1]);
            } else {
                throw new Error('无法从路径中提取年份，请显式提供年份参数');
            }
        }

        const allFiles = [];

        try {
            const files = readdirSync(vaultPath);
            files.forEach(file => {
                // 只处理.md文件，并且文件名符合周报格式（如：第1周12月30日.md）
                if (extname(file) === '.md' && file.match(/^第\d+周/)) {
                    const filePath = join(vaultPath, file);
                    allFiles.push({
                        fileName: file,
                        fullPath: filePath,
                        year: year
                    });
                }
            });
        } catch (error) {
            console.warn(`无法读取目录 ${vaultPath}: ${error.message}`);
        }

        // 按周数排序（提取文件名中的周数进行排序）
        allFiles.sort((a, b) => {
            const weekA = parseInt(a.fileName.match(/第(\d+)周/)[1]);
            const weekB = parseInt(b.fileName.match(/第(\d+)周/)[1]);
            return weekA - weekB;
        });

        return allFiles;
    } catch (error) {
        throw new Error(`获取周报文件列表失败: ${error.message}`);
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
 * 构建年度工作内容
 * @param {string} vaultPath - 周报所在的目录路径
 * @param {number} year - 年份（可选）
 * @param {boolean} debug - 调试模式
 * @returns {Object} 包含工作内容和统计信息的对象
 */
function buildYearContent(vaultPath, year = null, debug = false) {
    try {
        const allFiles = getAllWeeklyReports(vaultPath, year);
        let workContent = "";
        let errorMessages = [];
        let processedFiles = 0;
        let skippedFiles = 0;

        if (debug) {
            console.log(`找到 ${allFiles.length} 个周报文件`);
        }

        allFiles.forEach(fileInfo => {
            try {
                if (hasWorkContent(fileInfo.fullPath)) {
                    const fileContent = readFileSync(fileInfo.fullPath, "utf-8");
                    workContent += `<!-- 来自文件: ${fileInfo.fileName} -->\n`;
                    workContent += fileContent + "\n\n";
                    processedFiles++;

                    if (debug) {
                        console.log(`处理文件: ${fileInfo.fileName}`);
                    }
                } else {
                    skippedFiles++;
                    if (debug) {
                        console.log(`跳过文件: ${fileInfo.fileName} - 无工作内容`);
                    }
                }
            } catch (error) {
                const errorMsg = `文件无法读取: ${fileInfo.fileName} (路径: ${fileInfo.fullPath})`;
                errorMessages.push(errorMsg);
                console.warn(errorMsg);
            }
        });

        return {
            content: workContent,
            errors: errorMessages,
            stats: {
                totalFiles: allFiles.length,
                processedFiles: processedFiles,
                skippedFiles: skippedFiles
            }
        };
    } catch (error) {
        throw new Error(`构建年度工作内容失败: ${error.message}`);
    }
}

/**
 * 生成年度工作报告
 * @param {string} vaultPath - 周报所在的目录路径
 * @param {boolean} debug - 是否开启调试模式，默认为false
 * @returns {string} 生成的年度报告内容
 */
function buildYearReport(vaultPath, debug = false) {
    try {
        // 记录开始时间
        const startTime = new Date();

        // 检查是否提供了vaultPath
        if (!vaultPath) {
            throw new Error('未提供周报目录路径');
        }

        // 从路径中提取年份
        const yearMatch = vaultPath.match(/(\d{4})年/);
        if (!yearMatch) {
            throw new Error('路径中未包含年份信息（格式应为: .../XXXX年/）');
        }
        const year = parseInt(yearMatch[1]);

        // 构建调试信息
        let result = debug ? `<!-- 年度报告生成于 ${startTime.toLocaleString()} -->\n` : '';
        if (debug) {
            result += `<!-- 使用目录路径: ${vaultPath} -->\n`;
            result += `<!-- 目标年份: ${year} -->\n\n`;
        }

        // 构建年度工作内容
        const yearContentResult = buildYearContent(vaultPath, year, debug);

        // 添加统计信息（调试模式下）
        if (debug) {
            result += `<!-- 文件统计: 共找到${yearContentResult.stats.totalFiles}个文件，处理了${yearContentResult.stats.processedFiles}个有内容的文件，跳过${yearContentResult.stats.skippedFiles}个空文件 -->\n\n`;

            if (yearContentResult.errors && yearContentResult.errors.length > 0) {
                result += `<!-- 以下文件无法读取: \n${yearContentResult.errors.join('\n')}\n-->` + "\n\n";
            }
        }

        // 添加年度报告标题
        result += `# ${year}年度工作总结\n\n`;

        // 提取各类别内容
        const addContent = retrieveWorkContent(yearContentResult.content, "新增");
        const improveContent = retrieveWorkContent(yearContentResult.content, "优化");
        const fixContent = retrieveWorkContent(yearContentResult.content, "修正");

        // 组合最终结果
        result += addContent + "\n\n" + improveContent + "\n\n" + fixContent;

        // 生成输出文件路径
        const outputFileName = `${year}年度总结.md`;
        const outputPath = join(vaultPath, outputFileName);

        // 写入文件
        writeFileSync(outputPath, result, 'utf-8');

        // 添加完成信息（调试模式下）
        if (debug) {
            const endTime = new Date();
            result += `\n\n<!-- 年度报告生成完成于 ${endTime.toLocaleString()}，耗时 ${endTime - startTime}ms -->`;
            result += `\n<!-- 报告已保存至: ${outputPath} -->`;
            
            console.log(`年度报告已生成: ${outputPath}`);
            console.log(`处理统计: ${yearContentResult.stats.processedFiles}/${yearContentResult.stats.totalFiles} 个文件`);
        }

        return result;
    } catch (error) {
        // 捕获并返回所有错误
        const errorMessage = `# 【错误】年度报告生成失败\n\n
## 错误详情\n- 错误信息: ${error.message}\n- 当前使用的目录路径: ${vaultPath || '未提供'}\n\n
## 请检查\n1. 目录路径是否正确\n2. 周报文件格式是否符合要求\n3. 脚本是否有权限读取文件`;
        console.error(errorMessage);
        return errorMessage;
    }
}

/**
 * 年度报告生成脚本
 * @author 基于周报脚本扩展
 * @date 2025-01-18
 */
module.exports = buildYearReport;
module.exports.getAllWeeklyReports = getAllWeeklyReports;
module.exports.hasWorkContent = hasWorkContent;
module.exports.retrieveWorkContent = retrieveWorkContent;
