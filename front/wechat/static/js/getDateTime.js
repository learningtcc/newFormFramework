function getDay(day){  
  var today = new Date();  
  var targetday_milliseconds=today.getTime() + 1000*60*60*24*day; 
  today.setTime(targetday_milliseconds); //注意，这行是关键代码    
  return today;  
}  

function formatDate(date){
  var tYear = date.getFullYear();  
  var tMonth = date.getMonth();  
  var tDate = date.getDate();  
  return tYear + "-" + doHandleMonth(tMonth + 1) + "-" + doHandleMonth(tDate);  
}

function doHandleMonth(month){  
  var m = month;  
  if(month.toString().length == 1){  
    m = "0" + month;  
  }  
  return m;  
}  

function getTime(val){//转换为时间戳
  var date = val;
  date = new Date(Date.parse(date.replace(/-/g, "/")));
  date = date.getTime();
  return date;
}