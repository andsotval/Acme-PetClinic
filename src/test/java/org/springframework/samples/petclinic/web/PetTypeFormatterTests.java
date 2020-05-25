/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 17-may-2020
 */

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
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("hsqldb")
@ExtendWith(MockitoExtension.class)
class PetTypeFormatterTests {

	@Mock
	private PetTypeService		petTypeService;

	private PetTypeFormatter	petTypeFormatter;


	//TODO: Repasar todos los metodos
	@BeforeEach
	void setup() {
		petTypeFormatter = new PetTypeFormatter(petTypeService);
		Mockito.when(petTypeService.findAvailable()).thenReturn(makePetTypes());
	}

	@Test
	void testParsePositive() throws ParseException {
		PetType petType = petTypeFormatter.parse("Bird", Locale.ENGLISH);
		assertEquals("Bird", petType.getName());
	}

	@Test
	void TestThrowParseException() throws ParseException {
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
