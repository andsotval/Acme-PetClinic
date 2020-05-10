
package org.springframework.samples.petclinic.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.PetTypeService;

/**
 * Test class for {@link PetTypeFormatter}
 *
 * @author Colin But
 */
@ExtendWith(MockitoExtension.class)
class PetTypeFormatterTests {

	@Mock
	private PetTypeService		petTypeService;

	private PetTypeFormatter	petTypeFormatter;


	//TODO: Repasar todos los metodos
	@BeforeEach
	void setup() {
		petTypeFormatter = new PetTypeFormatter(petTypeService);
	}

	@Test
	void testPrint() {
		PetType petType = new PetType();
		petType.setName("Hamster");
		String petTypeName = petTypeFormatter.print(petType, Locale.ENGLISH);
		assertEquals("Hamster", petTypeName);
	}

	@Test
	void shouldParse() throws ParseException {
		Mockito.when(petTypeService.findAvailable()).thenReturn(makePetTypes());
		PetType petType = petTypeFormatter.parse("Bird", Locale.ENGLISH);
		assertEquals("Bird", petType.getName());
	}

	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(petTypeService.findAvailable()).thenReturn(makePetTypes());
		Assertions.assertThrows(ParseException.class, () -> {
			petTypeFormatter.parse("Fish", Locale.ENGLISH);
		});
	}

	/**
	 * Helper method to produce some sample pet types just for test purpose
	 *
	 * @return {@link Collection} of {@link PetType}
	 */
	private Collection<PetType> makePetTypes() {
		Collection<PetType> petTypes = new ArrayList<>();
		petTypes.add(new PetType() {

			{
				setName("Dog");
			}
		});
		petTypes.add(new PetType() {

			{
				setName("Bird");
			}
		});
		return petTypes;
	}

}
