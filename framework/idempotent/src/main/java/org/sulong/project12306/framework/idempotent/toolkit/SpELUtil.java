package org.sulong.project12306.framework.idempotent.toolkit;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpELUtil {
    public static Object parseKey(String spEL, Method method,Object[] contextObjects){
        ArrayList<String> list= new ArrayList<>(List.of("#","T(")) ;
        Optional<String> optional=list.stream().filter(spEL::contains).findFirst();
        if (optional.isPresent()){
            return parse(spEL, method, contextObjects);
        }
        return spEL;
    }

    private static Object parse(String spEL, Method method, Object[] contextObjects){
        SpelExpressionParser expressionParser=new SpelExpressionParser();
        Expression expression=expressionParser.parseExpression(spEL);
        StandardEvaluationContext context=new StandardEvaluationContext();
        DefaultParameterNameDiscoverer discoverer=new DefaultParameterNameDiscoverer();
        String[] params=discoverer.getParameterNames(method);
        if (ArrayUtil.isNotEmpty(params)){
            for (int len = 0; len < params.length; len++) {
                context.setVariable(params[len], contextObjects[len]);
            }
        }
        return expression.getValue(context);
    }
}
