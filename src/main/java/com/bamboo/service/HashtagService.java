package com.bamboo.service;

import com.bamboo.dto.HashtagDto;
import com.bamboo.entity.Hashtag;
import com.bamboo.repository.HashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class HashtagService {

    private final HashTagRepository hashtagRepository;

    public Hashtag saveHashtag(HashtagDto hashtagDto) {

        String hashtagName = hashtagDto.getHashtagName();

        Optional<Hashtag> existingHashtag = hashtagRepository.findByName(hashtagName);

        if (existingHashtag.isPresent()) {
            // 이미 존재하는 경우, 저장하지 않고 리턴합니다.
            return existingHashtag.get();
        }

        Hashtag newHashtag = new Hashtag();
        newHashtag.setName(hashtagName);

        return hashtagRepository.save(newHashtag);
    }

}
