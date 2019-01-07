package com.daniel.licenta.calendargenerator.business.common;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenericMapper<S, D> {

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    public D map(S source, D destination) {
        dozerBeanMapper.map(source, destination);
        return destination;
    }
}
