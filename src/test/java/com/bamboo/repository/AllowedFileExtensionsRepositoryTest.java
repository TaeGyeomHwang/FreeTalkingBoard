package com.bamboo.repository;

import com.bamboo.entity.AllowedFileExtensions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class AllowedFileExtensionsRepositoryTest {

    @Autowired
    AllowedFileExtensionsRepository allowedFileExtensionsRepository;

    @Test
    @DisplayName("파일 확장자명 찾기 테스트")
    public void findExtensions(){
        Long fileConfigId = 1L;

        List<AllowedFileExtensions> allowedFileExtensionsList =  allowedFileExtensionsRepository.findAllByFileConfigId(fileConfigId);

        for(AllowedFileExtensions allowedFileExtensions : allowedFileExtensionsList){
            System.out.println("extensions : " + allowedFileExtensions.getExtension());
        }

//        assertEquals(item.getId(), cartItem.getItem().getId());
//        assertEquals(cartItemDto.getCount(), cartItem.getCount());
    }
}