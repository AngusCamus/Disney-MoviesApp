package com.alkemy.disney.services.impl;

import com.alkemy.disney.dto.*;
import com.alkemy.disney.entities.CharacterEntity;
import com.alkemy.disney.exception.EnumErrors;
import com.alkemy.disney.exception.ParamNotFound;
import com.alkemy.disney.repositories.specifications.CharacterSpec;
import com.alkemy.disney.mappers.CharacterMapper;
import com.alkemy.disney.repositories.CharacterRepository;
import com.alkemy.disney.services.CharacterService;
import org.hibernate.QueryParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CharacterServiceImpl implements CharacterService {


    CharacterRepository characterRepository;
    CharacterMapper characterMapper;
    CharacterSpec characterSpec;

    @Autowired
    public CharacterServiceImpl(CharacterRepository characterRepository, CharacterMapper characterMapper, CharacterSpec characterSpec) {
        this.characterRepository = characterRepository;
        this.characterMapper = characterMapper;
        this.characterSpec = characterSpec;
    }

    @Override
    public CharacterDTO getById(Long id) {

        Optional<CharacterEntity> optChar = characterRepository.findById(id);
        if(!optChar.isPresent()) {
            throw new ParamNotFound(EnumErrors.ID_CHARACTER.getErrorMessage());
        }
        CharacterEntity character = optChar.get();
        CharacterDTO charDTO = characterMapper.characterEntity2DTO(character, true);

        return charDTO;
    }

    @Override
    public List<CharacterDTO> getAllCharacters() {
        List<CharacterEntity> entities = characterRepository.findAll();
        List<CharacterDTO> result = characterMapper.characterEntityList2DTOList(entities, true);
        return result;
    }

    @Override
    public CharacterDTO createCharacter(CharacterCreateDTO dto) {

        CharacterEntity entityNew = characterMapper.characterCreateDTO2Entity(dto);
        characterRepository.save(entityNew);
        CharacterDTO charSaved = characterMapper.characterEntity2DTO(entityNew, true);

        return charSaved;
    }

    @Override
    public void deleteCharacterById(Long id) {
        if(characterRepository.findById(id).isPresent()){
            characterRepository.deleteById(id);
        }else{
            throw new ParamNotFound(EnumErrors.ID_CHARACTER.getErrorMessage());
        }
    }

    @Override
    public CharacterDTO updateCharacter(CharacterUpdateDTO dto, Long id) {
        CharacterEntity entity = characterRepository.findById(id).get();
        CharacterEntity characterUpdate = characterMapper.characterUpdateDTO2EntityUpdated(dto, entity);
        CharacterEntity characterUpdated = characterRepository.save(characterUpdate);
        CharacterDTO dtoUpdated = characterMapper.characterEntity2DTO(characterUpdated, true);
        return dtoUpdated;
    }


    public List<CharacterBasicDTO> getAllCharacters(String name, Integer age, Set<Long> movies) {
        CharacterFilterDTO filterDTO = new CharacterFilterDTO(age, name, movies);

        List<CharacterEntity> entities = characterRepository.findAll(characterSpec.getByFilters(filterDTO));

        List<CharacterBasicDTO> characters = characterMapper.characterEntityList2BasicDTOList(entities);

        return characters;
    }


}
