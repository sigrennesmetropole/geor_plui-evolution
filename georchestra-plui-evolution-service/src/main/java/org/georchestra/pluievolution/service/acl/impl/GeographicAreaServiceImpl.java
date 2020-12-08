package org.georchestra.pluievolution.service.acl.impl;

import org.georchestra.pluievolution.core.dao.acl.GeographicAreaDao;
import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.core.dto.Point;
import org.georchestra.pluievolution.core.dto.User;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;
import org.georchestra.pluievolution.service.acl.GeographicAreaService;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.mapper.GeographicAreaMapper;
import org.georchestra.pluievolution.service.mapper.LocalizedMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GeographicAreaServiceImpl implements GeographicAreaService {

    @Autowired
    GeographicAreaDao geographicAreaDao;

    @Autowired
    GeographicAreaMapper geographicAreaMapper;

    @Autowired
    LocalizedMapper localizedMapper;

    @Override
    public GeographicArea getGeographicAreaByCodeInsee(String codeInsee) {
        return geographicAreaMapper.entityToDto(geographicAreaDao.findByCodeInsee(codeInsee));
    }

    @Override
    public GeographicArea getGeographicAreaByNom(String nom) {
        // Appliquer d'abord le formatage qu'il faut au nom avant de lancer la requete
        return geographicAreaMapper.entityToDto(geographicAreaDao.findByNom(nom));
    }

    @Override
    public List<GeographicArea> getAllGeographicArea() {
        return geographicAreaMapper.entitiesToDto(geographicAreaDao.findAll());
    }

    @Override
    public GeographicArea getCurrentUserArea() throws ApiServiceException {
        // On recupere l'organisation a laquelle appartient le user connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User details  = (User) authentication.getDetails();
        String nom = details.getOrganization();
        GeographicArea area = getGeographicAreaByNom(nom);
        if (area == null) {
            throw new ApiServiceException("Organisation inconnue", "404");
        }
        return area;
    }

    @Override
    public GeographicAreaEntity getGeographicAreaByPoint(Point point) {
        List<GeographicAreaEntity> geographicAreaEntities = geographicAreaDao.findAll();
        for(GeographicAreaEntity entity : geographicAreaEntities) {
            if (entity.getGeometry().intersects(localizedMapper.dtoToEntity(point)) && !entity.getCodeInsee().equals("243500139")) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public GeographicAreaEntity getGeographicAreaEntityByCodeInsee(String codeInsee) {
        return geographicAreaDao.findByCodeInsee(codeInsee);
    }
}
