package top.yonyong.sonar.pmd.lang.java.rule.custom;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.testframework.SimpleAggregatorTst;
import org.junit.Test;

import java.util.List;

public class MyFirstTestRuleTest extends SimpleAggregatorTst {

    private static final String RULESET = "java-custom";

    @Override
    public void setUp() {
        addRule(RULESET, "MyFirstTestRule");
    }

    @Test
    public void testMyFirstRule(){
        this.setUp();
        List<Rule> rules = getRules();
        runTests(rules.get(0), "MyFirstTestRule");
//        runTests(new MyFirstTestRule(), path);

//        ExtendRuleTst extendRuleTst = new ExtendRuleTst();
//        TestDescriptor testDescriptor = extendRuleTst.extractTestsFromJavaFile(new MyFirstTestRule(), path,"0");
//        TestDescriptor testDescriptor = extendRuleTst.extractTestsFromJavaFile(rules.get(0), "src/test/top/yonyong/sonar/pmd/custom/MyFirstTestRuleTest.java");
//        runTest(testDescriptor);
    }
}
