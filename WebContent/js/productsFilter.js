function createProductsFilter(){
		//Clear the content page from any data and reset it's size and margin
		var content = document.getElementById("content")
		while(content.firstChild){
			content.removeChild(content.firstChild);
		}
		content.style.marginLeft = "24%";
		
		var minReleaseYear = jsonFilter.minReleaseYear;
		var maxReleaseYear = jsonFilter.maxReleaseYear;
		var minDuration = jsonFilter.minDuration;
		var maxDuration = jsonFilter.maxDuration;
		var minBuyCost = jsonFilter.minBuyCost;
		var maxBuyCost = jsonFilter.maxBuyCost;
		var minRentCost = jsonFilter.minRentCost;
		var maxRentCost = jsonFilter.maxRentCost;
		var orderedBy = jsonFilter.orderedBy;
		var isAscending = jsonFilter.isAscending;
		var genres = jsonFilter.genres;
	    
	    //Update the filters elements
	    document.getElementById("filterName").value = ""; //Name input
	    document.getElementById("minReleaseYearSlider").min = minReleaseYear; //Release year sliders
	    document.getElementById("minReleaseYearSlider").max = maxReleaseYear;
	    document.getElementById("minReleaseYearSlider").value = minReleaseYear;
	    document.getElementById("minReleaseYear").innerHTML = minReleaseYear;
	    
	    document.getElementById("maxReleaseYearSlider").min = minReleaseYear; 
	    document.getElementById("maxReleaseYearSlider").max = maxReleaseYear;
	    document.getElementById("maxReleaseYearSlider").value = maxReleaseYear;
	    document.getElementById("maxReleaseYear").innerHTML = maxReleaseYear;
	    
	    document.getElementById("minDurationSlider").min = minDuration; //Duration sliders
	    document.getElementById("minDurationSlider").max = maxDuration;
	    document.getElementById("minDurationSlider").value = minDuration; 
	    document.getElementById("minDuration").innerHTML = minDuration;
	    
	    document.getElementById("maxDurationSlider").min = minDuration; 
	    document.getElementById("maxDurationSlider").max = maxDuration;
	    document.getElementById("maxDurationSlider").value = maxDuration;
	    document.getElementById("maxDuration").innerHTML = maxDuration;
	    
	    document.getElementById("minBuyCostSlider").min = minBuyCost; //Buy cost sliders
	    document.getElementById("minBuyCostSlider").max = maxBuyCost;
	    document.getElementById("minBuyCostSlider").value = minBuyCost; 
	    document.getElementById("minBuyCost").innerHTML = minBuyCost;
	    
	    document.getElementById("maxBuyCostSlider").min = minBuyCost; 
	    document.getElementById("maxBuyCostSlider").max = maxBuyCost;
	    document.getElementById("maxBuyCostSlider").value = maxBuyCost;
	    document.getElementById("maxBuyCost").innerHTML = maxBuyCost;
	    
	    document.getElementById("minRentCostSlider").min = minRentCost; //Rent cost sliders
	    document.getElementById("minRentCostSlider").max = maxRentCost;
	    document.getElementById("minRentCostSlider").value = minRentCost; 
	    document.getElementById("minRentCost").innerHTML = minRentCost;
	    
	    document.getElementById("maxRentCostSlider").min = minRentCost; 
	    document.getElementById("maxRentCostSlider").max = maxRentCost;
	    document.getElementById("maxRentCostSlider").value = maxRentCost;
	    document.getElementById("maxRentCost").innerHTML = maxRentCost;
	    
	    //Clear all genres
	    var genresSection = document.getElementById("genresSection");
	    genresSection.innerHTML = "";
	    
	    var genresContent = "";
	    
	    for(i in genres){
	    	genresContent += '<input class="w3-check productgenre" type="checkbox" id="'+genres[i].value+'"';
	    	genresContent += 'value="'+genres[i].id+'">'+genres[i].value+'</input><br>';
	    }
		genresSection.innerHTML = genresContent;
		
        //<input class="w3-check productgenre" type="checkbox"><label></label><br>
	    
	    
	    //Display the filters element
		var filters = document.getElementById("filters");
	    if (filters.className.indexOf("w3-show") == -1) {
	    	filters.className = filters.className.replace(" w3-hide", " w3-show");
	    }
	}

function updateProductFilter(){
	//Update the simple information objects
	jsonFilter.name = document.getElementById("filterName").value;
	jsonFilter.minReleaseYear = document.getElementById("minReleaseYearSlider").value;
	jsonFilter.maxReleaseYear = document.getElementById("maxReleaseYearSlider").value;
	jsonFilter.minDuration = document.getElementById("minDurationSlider").value;
	jsonFilter.maxDuration = document.getElementById("maxDurationSlider").value;
	jsonFilter.minBuyCost = document.getElementById("minBuyCostSlider").value;
	jsonFilter.maxBuyCost = document.getElementById("maxBuyCostSlider").value;
	jsonFilter.minRentCost = document.getElementById("minRentCostSlider").value;
	jsonFilter.maxRentCost = document.getElementById("maxRentCostSlider").value;
	jsonFilter.orderedBy;
	jsonFilter.isAscending;
	
	
	//Delete all previously checked genres
	//delete jsonFilter.genres;
	
	//Create the genres item in jsonFilter
	//collectGenresFromCheckboxes();
	
	return true;
}

function collectGenresFromCheckboxes(){
	var genres = document.getElementsByClassName("productgenre");
	
	for (i = 0; i < genres.length; i++) {
		if(genres[i].checked == true){
			var genreId = genres[i].value;
			var value = genres[i].id;
			
			jsonFilter.genres += '{"'+genreId+'":"'+value+'"}';
			if(i != genres.length - 1){
				jsonFilter.genres+=",";
			}
		}
	}
}