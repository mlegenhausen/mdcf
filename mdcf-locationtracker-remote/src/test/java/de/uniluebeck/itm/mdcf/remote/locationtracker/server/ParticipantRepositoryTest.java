package de.uniluebeck.itm.mdcf.remote.locationtracker.server;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

import de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain.GeoLocation;
import de.uniluebeck.itm.mdcf.remote.locationtracker.server.domain.Participant;

public class ParticipantRepositoryTest {

	private static final Module MODULE = new AbstractModule() {
		@Override
		protected void configure() {
			install(new JpaPersistModule("locationTrackerPersistenceUnit"));
		}
	};
	
	private static final Injector INJECTOR = Guice.createInjector(MODULE);
	
	private static final PersistService PERSIST_SERVICE = INJECTOR.getInstance(PersistService.class);
	
	private ParticipantRepository repository;
	
	@BeforeClass
	public static void beforeClass() {
		PERSIST_SERVICE.start();
	}
	
	@AfterClass
	public static void afterClass() {
		PERSIST_SERVICE.stop();
	}
	
	@Before
	public void setUp() {
		repository = INJECTOR.getInstance(ParticipantRepository.class);
	}
	
	@Test
	public void testAbstractRepository() {
		fail("Not yet implemented");
	}

	@Test
	public void testPersist() {
		Participant participant = new Participant();
		participant.setId("123");
		
		GeoLocation location = new GeoLocation();
		location.setTimestamp(System.currentTimeMillis());
		location.setLatitude(123.4);
		location.setLongitude(432.1);
		
		participant.getLocations().add(location);
		
		repository.persist(participant);
		
		assertNotNull(participant.getId());
		assertNotNull(location.getId());
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemove() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindById() {
		Participant participant = repository.findById("123");
		assertNotNull(participant);
	}

	@Test
	public void testFindAll() {
		fail("Not yet implemented");
	}

}
