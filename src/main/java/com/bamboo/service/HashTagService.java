package com.bamboo.service;

import com.bamboo.dto.HashtagDto;
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

    public Hashtag saveHashtag(HashtagDto hashtagDto) {
        Hashtag hashtag = new Hashtag();
        hashtag.setName(hashtagDto.getTagName());
        return hashTagRepository.save(hashtag);
    }
}
