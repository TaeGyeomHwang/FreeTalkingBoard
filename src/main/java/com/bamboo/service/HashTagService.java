package com.bamboo.service;

import com.bamboo.entity.Hashtag;
import com.bamboo.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashTagService {

    private final HashtagRepository hashtagRepository;

    public void save(Hashtag hashtag){
        hashtagRepository.save(hashtag);
    }
}
