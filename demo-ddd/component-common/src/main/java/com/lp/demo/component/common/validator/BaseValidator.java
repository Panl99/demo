package com.lp.demo.component.common.validator;

import jodd.vtor.Violation;
import jodd.vtor.Vtor;
import org.apache.commons.lang3.Validate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BaseValidator {

    public static <T> Validate verifyDto(T dto) {

        Vtor vtor = Vtor.create();

        vtor.validate(dto);

        List<Violation> vlist = vtor.getViolations();

        DefaultValidate validate = new DefaultValidate();

        if (CollectionUtils.isEmpty(vlist)) {
            validate.setPass(true);
        } else {
            validate.setPass(false);
            validate.setErrorList(vlist.stream().map(vr -> new ErrorBody(vr.getCheck().getName(), vr.getCheck().getMessage())).collect(Collectors.toList()));
        }
        return validate;
    }
}