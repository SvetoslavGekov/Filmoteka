	function loadUnauthorizedAccessModal(){
		//Make the modal visible
		document.getElementById(unauthorizedAccess).style.display='block';
	}
	
	function loadErrorModal(modal, paragraph, message){
		//Create a paragraph for the error message
		document.getElementById(paragraph).innerHTML = message;
		//Make the modal visible
		document.getElementById(modal).style.display='block';
	}
	
	function openOrCloseSection(id) {
	    var x = document.getElementById(id);
	    if (x.className.indexOf("w3-show") == -1) {
	        x.className = x.className.replace(" w3-hide", " w3-show");
	    }
	    else if(x.className.indexOf("w3-show") >= 1){ 
	        x.className = x.className.replace(" w3-show", " w3-hide");
	    }
	}

	function getSliderValue(slider, valueholder){
		document.getElementById(valueholder).innerHTML = document.getElementById(slider).value;
	}