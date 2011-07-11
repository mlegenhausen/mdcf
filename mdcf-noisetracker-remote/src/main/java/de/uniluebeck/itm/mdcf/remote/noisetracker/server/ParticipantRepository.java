package de.uniluebeck.itm.mdcf.remote.noisetracker.server;

import com.google.inject.Inject;

import de.uniluebeck.itm.mdcf.remote.noisetracker.server.domain.Participant;


public class ParticipantRepository extends AbstractRepository<Participant> {

	@Inject
	public ParticipantRepository() {
		super(Participant.class);
	}
}
