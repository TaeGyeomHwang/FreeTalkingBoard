package com.bamboo.service;

import com.bamboo.entity.Hashtag;
import com.bamboo.repository.HashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HashTagService {

    private final HashTagRepository hashTagRepository;

    public Hashtag findOrCreateTag(String tagName) {
        return hashTagRepository.findByName(tagName).orElseGet(() -> {
            Hashtag newTag = new Hashtag();
            newTag.setName(tagName);
            return hashTagRepository.save(newTag);
        });
    }
}
