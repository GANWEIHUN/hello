/**
 * YearReportCreator 模块测试脚本
 * 用于测试模块导出的 buildYearReport 函数
 */

// 导入需要测试的模块
const buildYearReport = require('./YearReportCreator');

/**
 * 打印测试结果
 * @param {string} title - 测试标题
 * @param {any} content - 测试内容
 */
function printTestResult(title, content) {
    console.log('====================================');
    console.log(`测试: ${title}`);
    console.log('====================================');

    if (typeof content === 'string') {
        // 如果是字符串，打印内容并显示长度
        console.log(content);
        console.log('------------------------------------');
        console.log(`内容长度: ${content.length} 字符`);
        console.log(`非空行数: ${content.split('\n').filter(line => line.trim()).length} 行`);
    } else {
        // 如果不是字符串，使用 JSON.stringify 打印
        console.log(JSON.stringify(content, null, 2));
    }

    console.log('\n');
}

/**
 * 验证年度报告内容是否有效
 * @param {string} content - 要验证的内容
 * @returns {boolean} 验证结果
 */
function validateYearReportContent(content) {
    if (typeof content !== 'string') {
        console.error('错误: 返回内容不是字符串类型');
        return false;
    }

    if (!content.trim()) {
        console.warn('警告: 返回内容为空');
        return false;
    }

    // 验证年度报告是否包含年度标题
    const expectedTitles = ['# 2025年度工作总结', '### 新增', '### 优化', '### 修正'];
    let allTitlesFound = true;

    expectedTitles.forEach(title => {
        if (!content.includes(title)) {
            console.warn(`警告: 年度报告内容不包含预期标题 ${title}`);
            allTitlesFound = false;
        }
    });

    return allTitlesFound;
}

/**
 * 测试文件遍历功能
 * @param {string} vaultPath - 周报目录路径
 */
function testFileTraversal(vaultPath) {
    console.log('开始测试文件遍历功能...');
    
    try {
        const getAllWeeklyReports = require('./YearReportCreator').getAllWeeklyReports;
        const files = getAllWeeklyReports(vaultPath);
        
        console.log(`找到 ${files.length} 个周报文件`);
        
        if (files.length > 0) {
            console.log('前5个文件:');
            files.slice(0, 5).forEach((file, index) => {
                console.log(`  ${index + 1}. ${file.fileName} (${file.month})`);
            });
            
            if (files.length > 5) {
                console.log(`  ... 还有 ${files.length - 5} 个文件`);
            }
        }
        
        return files.length > 0;
    } catch (error) {
        console.error('文件遍历测试失败:', error.message);
        return false;
    }
}

/**
 * 测试工作内容检测功能
 * @param {string} vaultPath - 周报目录路径
 */
function testWorkContentDetection(vaultPath) {
    console.log('开始测试工作内容检测功能...');
    
    try {
        const hasWorkContent = require('./YearReportCreator').hasWorkContent;
        const getAllWeeklyReports = require('./YearReportCreator').getAllWeeklyReports;
        
        const files = getAllWeeklyReports(vaultPath);
        let hasContentCount = 0;
        let noContentCount = 0;
        
        // 测试前3个文件
        files.slice(0, 3).forEach((file, index) => {
            const hasContent = hasWorkContent(file.fullPath);
            console.log(`  ${index + 1}. ${file.fileName}: ${hasContent ? '有内容' : '无内容'}`);
            
            if (hasContent) {
                hasContentCount++;
            } else {
                noContentCount++;
            }
        });
        
        console.log(`检测结果: ${hasContentCount} 个有内容, ${noContentCount} 个无内容`);
        return true;
    } catch (error) {
        console.error('工作内容检测测试失败:', error.message);
        return false;
    }
}

/**
 * 运行所有测试
 */
function runTests() {
    console.log('开始测试 YearReportCreator 模块\n');

    // 记录开始时间
    const startTime = Date.now();

    // 测试模块是否成功导入
    if (typeof buildYearReport !== 'function') {
        console.error('错误: 导入的不是函数类型');
        return;
    }

    console.log('模块导入成功，buildYearReport 是函数类型');
    console.log('\n');

    try {
        // 构建测试路径
        const path = require('path');
        const yearPath = path.join(__dirname, '..', '..', '周报', '2025年');
        console.log(`年度报告目录路径: ${yearPath}`);

        // 测试1: 文件遍历功能
        const traversalTestPassed = testFileTraversal(yearPath);
        console.log('\n');

        // 测试2: 工作内容检测功能
        const contentDetectionPassed = testWorkContentDetection(yearPath);
        console.log('\n');

        // 测试3: 年度报告生成功能（不调试模式）
        console.log('开始调用 buildYearReport 函数（不调试模式）...');
        const yearReportContent = buildYearReport(yearPath, false);
        console.log('函数调用完成\n');

        const yearReportValid = validateYearReportContent(yearReportContent);
        printTestResult('年度报告生成结果（不调试模式）', yearReportContent);

        // 测试4: 年度报告生成功能（调试模式）
        console.log('开始调用 buildYearReport 函数（调试模式）...');
        const yearReportContentDebug = buildYearReport(yearPath, true);
        console.log('函数调用完成\n');

        const yearReportDebugValid = validateYearReportContent(yearReportContentDebug);
        printTestResult('年度报告生成结果（调试模式）', yearReportContentDebug);

        // 统计测试结果
        const totalTests = 4;
        const passedTests = [traversalTestPassed, contentDetectionPassed, yearReportValid, yearReportDebugValid].filter(Boolean).length;

        // 记录结束时间
        const endTime = Date.now();
        const duration = endTime - startTime;

        console.log('====================================');
        console.log(`测试完成: 共 ${totalTests} 项, 通过 ${passedTests} 项`);
        console.log(`测试耗时: ${duration}ms`);
        console.log('====================================');
        
        if (passedTests === totalTests) {
            console.log('🎉 所有测试通过！年度报告生成功能正常。');
        } else {
            console.log('⚠️  部分测试未通过，请检查相关功能。');
        }
    } catch (error) {
        console.error('错误: 调用 buildYearReport 函数时发生异常:', error);
    }
}

// 执行测试
runTests();
