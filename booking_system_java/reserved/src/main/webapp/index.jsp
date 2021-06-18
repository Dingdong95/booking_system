<%@ page language ="java" contentType = "text/html; charset = UTF-8"
pageEncoding ="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>index</title>
    <link rel="stylesheet" href="css/index.css" />
  </head>
  <body>
    <div>
      <div class="header">
        <div>
          <span>즐겨찾기</span>
          <span>입점신청<i class="fas fa-sort-down"></i></span>
        </div>
        <div>
          <span>로그인</span>
          <span>회원가입</span>
          <span>고객센터</span>
        </div>
      </div>

      <div class="search_menu">
        <div class="main_btn" id="cat_btn">
          <i class="fas fa-bars fa-2x"></i>카테고리
        </div>

        <div id="search_box">
          <div class="search_box_option">
            <div id="main_logo"><img src="img/main_logo.JPG" /></div>
            <div class="search_bar">
              <select>
                <option value="전체">전체</option>
                <option value="여성패션">여성패션</option>
                <option value="남성패션">남성패션</option>
                <option value="남녀 공용 의류">남녀 공용 의류</option>
                <option value="유아동패션">유아동패션</option>
                <option value="뷰티">뷰티</option>
                <option value="출산/유아동">출산/유아동</option>
                <option value="식품">식품</option>
                <option value="주방">주방</option>
              </select>
              <input
                id="search"
                type="search"
                value="찾고 싶은 상품을 검색해보세요!"
              />
              <i class="fas fa-microphone fa-lg"></i>
              <i class="fas fa-search fa-lg"></i>
            </div>
          </div>

          <div class="nav">
            <img src="img/rocket.JPG" />
            <img src="img/rocket2.JPG" />
            <span>로켓직구</span>
            <span>골드박스</span>
            <span>정기배송</span>
            <span>이벤트/쿠폰</span>
            <span>기획전</span>
            <span>여행/티켓</span>
          </div>
        </div>

        <div class="main_btn"><i class="far fa-user fa-2x"></i>마이쿠팡</div>

        <div class="main_btn">
          <i class="fas fa-shopping-cart fa-2x"></i>장바구니
        </div>
      </div>

      <div>4</div>
      <div>5</div>
    </div>
  </body>
  <script
    src="https://kit.fontawesome.com/cbb21f57d4.js"
    crossorigin="anonymous"
  ></script>
</html>