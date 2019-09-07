package com.swisscom.hero.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.swisscom.heroes.model.Hero;

public class heroesDS {
	private final List<Hero> source;
	private int last;

	public heroesDS(){
		source=new ArrayList<Hero>();
		source.add(new Hero.Builder().id(1).name("Pupa").createHero());
		source.add(new Hero.Builder().id(2).name("Nani").createHero());
		source.add(new Hero.Builder().id(3).name("Mausi").createHero());
		source.add(new Hero.Builder().id(4).name("Nico").createHero());
		source.add(new Hero.Builder().id(5).name("Verita").createHero());
		source.add(new Hero.Builder().id(6).name("Eugenio").createHero());
		last=6;
	}

	public Hero add(Hero hero) {
		do {
			last++;
		}while(source.stream().anyMatch(x-> {return (last==x.getId());}));

		final Hero val=new Hero.Builder().id(last).name(hero.getName()).createHero();

		source.add(val);

		return val;
	}

	public boolean update(Hero hero) {
		final Optional<Hero> val=source.stream().filter((Hero x)-> x.getId()==hero.getId()).findFirst();

		if(val.isPresent()) {
			source.remove(val.get());
			source.add(hero);
			return true;
		}
		return false;
	}

	public boolean delete(Hero hero) {
		final Optional<Hero> val=source.stream().filter((Hero x)-> x.getId()==hero.getId()).findFirst();

		if(val.isPresent()) {
			source.remove(val.get());
			return true;
		}
		return false;
	}	

	public Hero find(int Id) {
		final Optional<Hero> val=source.stream().filter((Hero x)-> x.getId()==Id).findFirst();

		if(val.isPresent()) {
			return val.get();
		}
		return null;
	}	

	public List<Hero> list() {
		return source;
	}	
}
