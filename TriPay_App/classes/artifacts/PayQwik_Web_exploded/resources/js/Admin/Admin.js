var baseUrl=$(location).attr('protocol')+"//"+$(location).attr('host');

//Table
$(document).ready(function(){
	$('#table').dataTable();
});

function toggleCheckBox(id){
	if($('#'+id).is(':checked')){
		$('#'+id).val("true");
	}
	else{
		$('#'+id).val("false");
	}
}