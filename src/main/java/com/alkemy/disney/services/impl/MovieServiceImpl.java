package com.alkemy.disney.services.impl;

import com.alkemy.disney.dto.MovieBasicDTO;
import com.alkemy.disney.dto.MovieFilterDTO;
import com.alkemy.disney.entities.CharacterEntity;
import com.alkemy.disney.exception.ParamNotFound;
import com.alkemy.disney.repositories.CharacterRepository;
import com.alkemy.disney.repositories.specifications.MovieSpec;
import com.alkemy.disney.dto.MovieDTO;
import com.alkemy.disney.dto.MovieUpdateDTO;
import com.alkemy.disney.entities.MovieEntity;
import com.alkemy.disney.mappers.MovieMapper;
import com.alkemy.disney.repositories.MovieRepository;
import com.alkemy.disney.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    MovieRepository movieRepository;
    MovieMapper movieMapper;
    MovieSpec movieSpec;

    CharacterRepository characterRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper, MovieSpec movieSpec, CharacterRepository characterRepository) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.movieSpec = movieSpec;
    }

    @Override
    public MovieDTO createMovie(MovieDTO dto) {
        MovieEntity entity = movieMapper.movieDTO2Entity(dto);
        MovieEntity entitySaved = movieRepository.save(entity);
        MovieDTO movieSaved = movieMapper.movieEntity2DTO(entitySaved, true);

        return movieSaved;
    }

    @Override
    public MovieDTO getOneById(Long id) {

        Optional<MovieEntity> optMovie = movieRepository.findById(id);
        if(!optMovie.isPresent()) {
            throw new ParamNotFound("Id movie not found");
        }
        MovieEntity movieEntity = optMovie.get();
        MovieDTO movie = movieMapper.movieEntity2DTO(movieEntity, true);

        return movie;
    }

    @Override
    public List<MovieDTO> getAllMovies() {

        List<MovieEntity> entities = movieRepository.findAll();
        List<MovieDTO> result = movieMapper.movieEntityList2DTOList(entities, true);
        return result;
    }

    @Override
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    @Override
    public MovieDTO updateMovie(MovieUpdateDTO dto, Long id) {
        Optional<MovieEntity> optMovie = movieRepository.findById(id);
        MovieEntity entity = optMovie.get();
        MovieEntity entityUpdated = movieMapper.movieUpdateDTO2Entity(dto, entity);
        MovieDTO movieUpdated = movieMapper.movieEntity2DTO(entityUpdated, true);
        return movieUpdated;
    }

    @Override
    public List<MovieBasicDTO> getAllMovies(String name, String genre, String order) {
        MovieFilterDTO filterDTO = new MovieFilterDTO(name, genre, order);
        List<MovieEntity> moviesEntity = movieRepository.findAll(movieSpec.getByFilters(filterDTO));
        List<MovieBasicDTO> movies = movieMapper.movieEntityList2BasicDTOList(moviesEntity);

        return movies;
    }

    @Override
    public MovieDTO addCharacter2Movie(Long id, Long idCharacter) {

        Optional<MovieEntity> optMovie = movieRepository.findById(id);
        if(!optMovie.isPresent()) {
            throw new ParamNotFound("Id movie not found");
        }
        MovieEntity entity = optMovie.get();

        Optional<CharacterEntity> optCharacter = characterRepository.findById(idCharacter);
        if(!optCharacter.isPresent()) {
            throw new ParamNotFound("Id character not found");
        }
        CharacterEntity character = optCharacter.get();
        entity.addCharacter(character);
        MovieDTO movie = movieMapper.movieEntity2DTO(entity, true);
        return movie;
    }
}