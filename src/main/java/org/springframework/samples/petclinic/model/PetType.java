/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 03-mar-2020
 * User: carlo
 */

package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Juergen Hoeller Can be Cat, Dog, Hamster...
 */
@Entity
@Table(name = "pet_type")
public class PetType extends NamedEntity {

}
