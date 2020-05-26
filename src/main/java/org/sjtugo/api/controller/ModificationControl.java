package org.sjtugo.api.controller;

import io.swagger.annotations.Api;
import org.sjtugo.api.DAO.ModificationRepository;
import org.sjtugo.api.entity.Modification;
import org.sjtugo.api.service.ModificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value="修改记录信息")
@RestController  // the data returned by each method will be written straight into the response body
@RequestMapping("/modify")
public class ModificationControl {
    @Autowired
    private ModificationRepository modificationRepository;

    @GetMapping()
    public List<Modification> getModification(@RequestParam Integer adminID) {
        ModificationService modiService = new ModificationService(modificationRepository);
        return modiService.getModification(adminID);
    }
}
