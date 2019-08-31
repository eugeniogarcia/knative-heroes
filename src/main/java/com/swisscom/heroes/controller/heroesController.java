package com.swisscom.heroes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.swisscom.hero.datasource.heroesDS;
import com.swisscom.heroes.filter.RequestContext;
import com.swisscom.heroes.model.Hero;

@RestController
@RequestMapping(value = "/")
public class heroesController {

	@Autowired
	private heroesDS servicio;

	@Autowired
	private RequestContext context;

	@RequestMapping(value = "/heroes",produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<List<Hero>> getHeroes(){
		if(this.context.isFail()) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else {
			return new ResponseEntity<>(servicio.list(),HttpStatus.OK);
		}
	}


	@RequestMapping(value = "/heroes/{id}",produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<Hero> getHero(@PathVariable("id") Integer id){
		final Hero resp=servicio.find(id);
		if(resp==null) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(resp,HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/heroes",produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<Hero> addHero(@RequestBody Hero hero){
		final Hero val=servicio.add(hero);
		if(val!=null) {
			return new ResponseEntity<>(val,HttpStatus.OK); 
		}
		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
		}
	}

	@RequestMapping(value = "/heroes",produces = { "application/json" }, method = RequestMethod.PUT)
	public ResponseEntity<Void> updateHero(@RequestBody Hero hero){
		if(servicio.update(hero)) {
			return new ResponseEntity<>(HttpStatus.OK); 
		}
		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
		}
	}

	@RequestMapping(value = "/heroes/{id}",produces = { "application/json" },method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteHero(@PathVariable int id){
		final Hero resp=servicio.find(id);
		if(resp==null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if(servicio.delete(resp)) {
			return new ResponseEntity<>(HttpStatus.OK); 
		}
		else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
		}
	}


	@RequestMapping(value = "/public/heroes",produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity<Hero> addPublicHero(@RequestBody Hero hero){
		return addHero(hero);
	}

	@RequestMapping(value = "/public/heroes",produces = { "application/json" }, method = RequestMethod.PUT)
	public ResponseEntity<Void> updatePublicHero(@RequestBody Hero hero){
		return updateHero(hero);
	}

	@RequestMapping(value = "/public/heroes/{id}",produces = { "application/json" },method = RequestMethod.DELETE)
	public ResponseEntity<Void> deletePublicHero(@PathVariable int id){
		return deleteHero(id);
	}

}
