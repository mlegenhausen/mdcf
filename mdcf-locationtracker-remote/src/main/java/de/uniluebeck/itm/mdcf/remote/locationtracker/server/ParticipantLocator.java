package de.uniluebeck.itm.mdcf.remote.locationtracker.server;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Locator;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain.Participant;

public class ParticipantLocator extends Locator<Participant, String> {

	private final ParticipantRepository repository;
	
	@Inject
	public ParticipantLocator(ParticipantRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public Participant create(Class<? extends Participant> clazz) {
		return new Participant();
	}

	@Override
	public Participant find(Class<? extends Participant> clazz, String id) {
		return repository.findById(id);
	}

	@Override
	public Class<Participant> getDomainType() {
		return Participant.class;
	}

	@Override
	public String getId(Participant participant) {
		return participant.getId();
	}

	@Override
	public Class<String> getIdType() {
		return String.class;
	}

	@Override
	public Object getVersion(Participant domainObject) {
		return 0;
	}

}
