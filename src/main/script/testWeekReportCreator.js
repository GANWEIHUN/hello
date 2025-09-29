/**
 * WeekReportCreator 模块测试脚本
 * 用于测试模块导出的 buildWeekReport 函数
 */

// 导入需要测试的模块（现在直接导入函数）
const buildWeekReport = require('./WeekReportCreator');

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
 * 验证内容是否有效
 * @param {string} content - 要验证的内容
 * @returns {boolean} 验证结果
 */
function validateContent(content) {
    if (typeof content !== 'string') {
        console.error('错误: 返回内容不是字符串类型');
        return false;
    }

    if (!content.trim()) {
        console.warn('警告: 返回内容为空');
        return false;
    }

    // 验证weekReport是否包含所有三个分类的标题
    const expectedTitles = ['### 新增', '### 优化', '### 修正'];
    let allTitlesFound = true;

    expectedTitles.forEach(title => {
        if (!content.includes(title)) {
            console.warn(`警告: 周报内容不包含预期标题 ${title}`);
            allTitlesFound = false;
        }
    });

    return allTitlesFound;
}

/**
 * 运行所有测试
 */
function runTests() {
    console.log('开始测试 WeekReportCreator 模块\n');

    // 记录开始时间
    const startTime = Date.now();

    // 测试模块是否成功导入
    if (typeof buildWeekReport !== 'function') {
        console.error('错误: 导入的不是函数类型');
        return;
    }

    console.log('模块导入成功，buildWeekReport 是函数类型');
    console.log('\n');

    try {
        // 测试 buildWeekReport 函数调用
        console.log('开始调用 buildWeekReport 函数...');
        const weekReportContent = buildWeekReport(__dirname, true);
        console.log('函数调用完成\n');

        // 验证返回的周报内容
        const weekReportValid = validateContent(weekReportContent);
        printTestResult('weekReport 生成结果', weekReportContent);

        // 统计测试结果
        const totalTests = 1;
        const passedTests = weekReportValid ? 1 : 0;

        // 记录结束时间
        const endTime = Date.now();
        const duration = endTime - startTime;

        console.log('====================================');
        console.log(`测试完成: 共 ${totalTests} 项, 通过 ${passedTests} 项`);
        console.log(`测试耗时: ${duration}ms`);
        console.log('====================================');
    } catch (error) {
        console.error('错误: 调用 buildWeekReport 函数时发生异常:', error);
    }
}

// 执行测试
runTests();
