package org.springframework.samples.petclinic.web;


import org.springframework.beans.factory.annotation.Autowired;

class StayControllerTests {

	@Autowired
	private StayController stayController;
	
	//listAllPending (todas las stays devueltas tienen que tener isAcepted a null)
	
	//listAllAccepted (todas las stays devueltas tienen que tener isAcepted a true)
	
	//acceptStay (pasarle una stay con isAccepted a null y te la actualice a true)
	
	//cancelStay (pasarle una stay con isAccepted a null y te la actualice a false)
	
	//changeDateStay (la stay que entra es la misma que sale)
	
	//updateStay (actualizar parametros (startDate, finishDate y description) y comprobar que se ha guardado bien)
	//startDate tiene que estar en futuro
	//diferencia entre startdate y finishDate minimo de un dia, maximo siete



}
