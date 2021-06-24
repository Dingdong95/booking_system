function callType(object){
	// Click된 Div의 선택(색상 바꾸기):: default: #F6F6F6 << bg >> #FFBB00
	// general  restaurant
	let accessType = document.getElementsByName("accessType")[0];
	let objectId;
	if(object.id == "general"){
		objectId = "restaurant";
		accessType.value = "G";
	}else{
		objectId = "general";
		accessType.value = "R";
	}
	
	object.className = "choiceOn";
	document.getElementById(objectId).className = "choiceOff";

}

/* 회원가입 유형에 따른 CSS */
function joinType(object){
	let accessType = document.getElementsByName("accessType")[0];
	
	let objectId;
	if(object.id == "general"){
		objectId = "restaurant";
		accessType.value = "G";
		
		uCode.innerText = "User Code";
		//uCode.placeholder = "Your ID";
		uName.innerText = "User Name";
		//uName.placeholder = "Your Name";	
		uEtc.innerText = "User Phone";
		//uEtc.placeholder = "Your Phone";
		
		sel.style.display = "none";
	}else{
		objectId = "general";
		accessType.value = "R";
		// innerHtml | innerText
		uCode.innerText = "Restaurant Code";
		//uCode.placeholder = "레스토랑 코드";
		uName.innerText = "Restaurant Name";
		//uName.placeholder = "상호명";	
		uEtc.innerText = "CEO Name";
		//uEtc.placeholder = "대표자";
		
		sel.style.display = "block";			
	}
	
	object.className = "choiceOn";
	document.getElementById(objectId).className = "choiceOff";
}

function isIdCheck(word, type){
	const cuComp = /^[A-Z]{1}[A-Z0-9]{4,9}$/;
	const reComp = /^[0-9]{10}$/;
	let result;

	if(type){
		result = cuComp.test(word);
	}else{
		result = reComp.test(word);
	}
	
	return result;
}

function isPasswordCheck(word){
	const sEng = /[a-z]/;
	const bEng = /[A-Z]/;
	const num = /[0-9]/;
	const special = /[!@#$%^&*]/; 
	
	// password가 영문소문자, 영문대문자, 숫자, 특수문자 중 3가지 이상의 문자군을 사용했는지 여부
	let count = 0;
	if(sEng.test(word)){	count++; }
	if(bEng.test(word)){	count++; }
	if(num.test(word)){	count++; }	
	if(special.test(word)){	count++; }
	
	return count;
}

function charCount(word, min, max){
	return word.length >= min && word.length <= max;
}

function sendAccessInfo(){
	// html 객체 연결 : id, password
	let id = document.getElementsByName("uCode")[0];
	let password = document.getElementsByName("aCode")[0];
	let accessType = document.getElementsByName("accessType")[0];
	// id가 영어소문자로 시작하고 숫자를 포함할 수 있으면서 전체 길이는 5~10
	
	if(accessType.value == "G"){
		if(!isIdCheck(id.value, true)) {
			id.value="";
			id.focus();
			return;
		}
	}else{
		if(!isIdCheck(id.value, false)) {
			id.value="";
			id.focus();
			return;
		}
	}
	
	if(isPasswordCheck(password.value) < 3){
		password.value="";
		password.focus();
		return;
	}
	
	// password 길이 파악 : 7~12
	if(!charCount(password.value, 7, 12)){
		password.value = "";
		password.focus();
		return;
	}
	
	// form 객체 생성
	let f = document.createElement("form");
	f.method = "post"; //f.setAttribute("method", "post");
	f.action = "LogIn";
	// id와 password를 form자식으로 입양
	f.appendChild(id);
	f.appendChild(password);
	f.appendChild(accessType);
	// form을 body자식으로 입양
	document.body.appendChild(f);
	
	f.submit();
}

function korCheck(obj, event){
	const pattern = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;
	
	if(pattern.test(event.target.value.trim())) {
		obj.value = obj.value.replace(pattern,'').trim();
	}
}

function btnCss(object, state){
	object.style.backgroundColor = state? "#FFBB00":"#F6F6F6";
	object.style.color = state? "#FFFFFF":"#2478FF";
}

function dupCheck(obj){
	let uCode = document.getElementsByName("uCode")[0];
	let accessType = document.getElementsByName("accessType")[0];
	
	if(obj.value != "재입력"){
		// 아이디 유효성 검사
		if(accessType.value == "G"){
			if(!isIdCheck(uCode.value, true)){
				uCode.value = "";
				uCode.focus();
				return;
			}
		}else{
			if(!isIdCheck(uCode.value, false)){
			uCode.value = "";
			uCode.focus();
			return;
			}
		}
		
		let f = document.createElement("form");
		f.method = "post"; 
		f.action = "DupCheck";
		
		f.appendChild(uCode); 
		f.appendChild(accessType); 
		document.body.appendChild(f);
		f.submit();
	}else{
		uCode.value = "";
		uCode.readOnly = false;
		uCode.focus();
		obj.value = "중복검사";	
	}
}

function sendJoinInfo(){
	// HTML Object 연결
	let uCode = document.getElementsByName("uCode")[0];
	let aCode = document.getElementsByName("aCode");
	let uName = document.getElementsByName("uName")[0];
	let uPhone = document.getElementsByName("uPhone")[0];
	let accessType = document.getElementsByName("accessType")[0];
	
	/* Restaurant 가입 시*/
	if(accessType.value == "R"){
		let location = document.getElementsByName("location")[0];
		let rType = document.getElementsByName("rType")[0];
	}
	
	// 패스워드 유효성 체크 // 패스워드 일치여부 체크
	if(isPasswordCheck(aCode[0].value) < 3 || aCode[0].value != aCode[1].value){
		aCode[0].value=""; aCode[1].value="";
		aCode[0].focus();
		return;
	}
	
	// 사용자명 입력여부 체크
	if(uName.value == ""){
		uName.focus();
		return;
	}
	
	// 분류와 지역선택 여부
	if(accessType.value == "R"){
		if(location.value == "지역선택"){ // location.selectedIndex < 1
			location.focus();
			return;
		}
		if(rType.value == "분류선택"){ // rType.selectedIndex < 1
			rType.focus();
			return;
		}
	}
	
	// form 생성
	let f = document.createElement("form");
	f.method = "post";
	f.action = "Join";
	// 아이디, 패스워드, 사용자명, 사용자전화번호 를 form의 자식으로 편입
	f.appendChild(accessType);
	f.appendChild(uCode); f.appendChild(aCode[0]);
	f.appendChild(uName); f.appendChild(uPhone);
	
	if(accessType.value == "R"){
		f.appendChild(location); 
		f.appendChild(rType);
	}
	
	// form을 body의 자식으로 편입
	document.body.appendChild(f);
	// 전송
	f.submit();
}

function isDupCheck(message, userId){
	let uCode = document.getElementsByName("uCode")[0];
	let dupBtn = document.getElementById("dupBtn");
	if(message != ""){
		let result = confirm(message + "사용하시겠습니까?");
		if(result){
			uCode.value = userId;
			uCode.readOnly = true;
			dupBtn.value = "재입력";
		}
	}
}

function callMessage(message){
	if(message != ""){
		alert(message);
	}
}

function search(){
	// HTML Object 연결 getElementsByName("word")[0]
	let word = document.getElementsByName("word")[0];
	// 검색어 입력 여부 확인
	if(word.value == ""){ 
		alert("검색어를 입력해주세요");
		word.focus();
		return;
	}
	
	// Form 개체 생성  >> method:GET  action:Search
	let f = document.createElement("form");
	f.method = "get";
	f.action = "Search";
	
	// Form 개체 : HTML Object를 자식으로 편성
	f.appendChild(word);
	// Form 개체 : body의 자식으로 편성
	document.body.appendChild(f);
	// Form 전송
	f.submit();
}

/* 예약 */
function reserve(rCode){
	let reCode = makeInput("text", "reCode", rCode)
	let f = document.createElement("form");
	f.action = "Reserve";
	f.method = "post";
	f.appendChild(reCode);
	
	document.body.appendChild(f);
	f.submit();
}

function rMain_init(message){
	if(message != "") alert(message);
	let list = document.getElementsByName("list");
	list[1].style.display = "none";
	
}

function selectRestaurant(cate, reCode, cuCode, reDate, mCode){
	if(cate){
		let check = confirm("예약을 확정하시겠습니까?");
		if(!check) return;
	
		// input 개체 생성
		// form 생성
		let f = document.createElement("form");
		f.action = "ConfirmReserve";
		f.method = "post";
	
		f.appendChild(makeInput("hidden", "reCode", reCode));
		f.appendChild(makeInput("hidden", "cuCode", cuCode));
		f.appendChild(makeInput("hidden", "dbDate", reDate));
	
		document.body.appendChild(f);
	
		f.submit();
	}else{
		alert("금일예약현황");
	}
}

function makeInput(type, name, value){
	let input = document.createElement("input");
	input.type = type;
	input.name = name;
	input.value = value;
	
	return input;
}

function showDiv(index){
	let list = document.getElementsByName("list");
	
	if(index){
		list[0].style.display = "block";
		list[1].style.display = "none";
	}else{
		list[0].style.display = "none";
		list[1].style.display = "block";
	}
	
}

function trOver(comments, obj){
	obj.className = "mouseOver";
}
function trOut(obj){
	obj.className = "mouseOut";
}

function callMenu(reCode, day){
	// step1 
	let ajax = new XMLHttpRequest();
	// step2 
	ajax.onreadystatechange = function(){
		if(ajax.readyState == 4 && ajax.status == 200){
		//step 5 
		alert(ajax.responseText);	
		}
	}
	// step3 
	ajax.open("GET","Step2?reCode="+reCode+"&date="+day,true);
	//step 4
	ajax.send();
	
}

