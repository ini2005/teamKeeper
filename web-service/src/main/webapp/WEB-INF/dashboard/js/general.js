'use strict';

//Get time in millis and return it in a date format dd-mm-yyyy hh:mm:ss
function milliToDateWithSeconds(milli) {

	var date = new Date(milli);
	
	var month = date.getMonth()+1;
	
	if (month < 10){
		month = "0" + month;
	}
	
	var day = date.getDate();
	if (day < 10){
		day = "0" + day;
	}
	
	var hours = date.getHours();
	if (hours < 10){
		hours = "0" + hours;
	}
	
	var minutes = date.getMinutes();
	if (minutes < 10){
		minutes = "0" + minutes;
	}
	
	var seconds = date.getSeconds();
	if (seconds < 10){
		seconds = "0" + seconds;
	}
    return (month + '-' + day +  '-' + date.getFullYear() + ' ' + hours + ':' + minutes + ':' + seconds);
}