package top.yonyong.sonar.pmd.lang.java.rule.custom;

import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import top.yonyong.sonar.pmd.lang.java.rule.AbstractAliRule;

/**
 * MyFirstTestRule
 *
 * @author yonyong
 * @date 2022/11/13
 */
public class MyFirstTestRule extends AbstractAliRule {

    private static final String ERR_METHOD_NAME = "NonCompliantEquals";

    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {
        String image = node.getMethodName().toLowerCase();
        if (image.contains(ERR_METHOD_NAME.toLowerCase())) {
            addViolationWithMessage(data, node,
                    "java.custom.MyFirstTestRule.rule.msg", new Object[]{image});
        }
        return super.visit(node, data);
    }

}
