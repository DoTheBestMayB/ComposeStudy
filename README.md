# 📚 무슨 Repository 인가요?

이 Repository는 Philipp Lackner의 Compose Masterclass 강의를 들으며 학습한 내용을 정리한 공간입니다.

# 📖 학습 내용 정리

## 🔗 목차
- [Basic Layout & Responsiveness](#basic-layout--responsiveness)
    - [Flow Layout](#flow-layout)
    - [LazyList](#lazylist)
    - [LazyGrid](#lazygrid)
    - [Scaffold](#scaffold)
    - [ConstraintLayout](#constraintlayout)
    - [WindowSizeClass](#windowsizeclass)
- [배운 내용 응용해서 구현하기](#배운-내용-응용해서-구현하기)

---

## 🏗️ Basic Layout & Responsiveness

### 📌 Flow Layout
🔗 [FlowLayoutDemo.kt](https://github.com/DoTheBestMayB/ComposeStudy/blob/master/app/src/main/java/com/dothebestmayb/composestudy/basic_layout/FlowLayoutDemo.kt)

줄에 표시할 최대 아이템 개수를 지정하고, 해당 개수보다 많아지면 다음 줄에 표시함.
`overflow` 파라미터를 이용해 표시할 최대 줄 수를 조절할 수 있음.

<img src="https://github.com/user-attachments/assets/97243660-c55a-49a4-939d-b0998bafbcca" width=300 />

---

### 📌 LazyList
🔗 [LazyListDemo.kt](https://github.com/DoTheBestMayB/ComposeStudy/blob/master/app/src/main/java/com/dothebestmayb/composestudy/basic_layout/LazyListDemo.kt)

- `modifier`의 `padding`은 `margin`처럼 동작한다.  `contentPadding`은 일반적으로 말하는 `padding`처럼 동작.
- `RecyclerView`와 유사한 방식으로 동작.
- `items` : 여러 개의 아이템을 index에 따라 표시할 때 사용
- `item` : footer처럼 단독 아이템을 표시할 때 사용
- `stickyHeader` : 스크롤 시 특정 헤더를 상단에 고정할 수 있음. 다른 stickyHeader가 상단에 배치되면 이전 stickyHeader는 더 이상 표시되지 않음. 연락처에서 이름의 초성을 나타낼 때 쓸 수 있음. ex) ㄱ, ㄴ, ㄷ, ...

<img src="https://github.com/user-attachments/assets/77b8e2fa-4c26-473a-899f-8ee814e7946e" width=300 />

---

### 📌 LazyGrid
🔗 [LazyGridDemo.kt](https://github.com/DoTheBestMayB/ComposeStudy/blob/master/app/src/main/java/com/dothebestmayb/composestudy/basic_layout/LazyGridDemo.kt)

박스 형식으로 아이템을 쌓는 Layout
`LazyVerticalGrid` : 가로로 아이템을 쌓은 후, Grid Layout의 width를 벗어나면 다음 가로 줄에 아이템을 쌓는다.

Tip : LazyVerticalGrid에서 사용되는 item의 width 속성은 일반적으로 무시된다. Modifier를 이용해 width를 설정해도 LazyVerticalGrid의 `columns` 파라미터로 설정한 크기 적용된다. 이 부분은 [Constraint 개념](https://github.com/DoTheBestMayB/ComposeStudy/blob/master/app/src/main/java/com/dothebestmayb/composestudy/measurements/SizeModifiersDemo.kt) 때문이다.

`LazyVerticalStaggeredGrid` : LazyGrid와 달리 각 아이템이 다양한 Height을 가질 수 있다. width 속성이 무시되는 것은 동일하다.

<img src="https://github.com/user-attachments/assets/9be038cc-5324-4372-be1a-411220077b66" width=600 />

LazyVerticalGrid에서도 각 아이템이 다양한 Height를 가질 수는 있으나, 다음 줄의 각 아이템의 시작점은 이전 줄에서 길이가 가장 긴 아이템보다 아래에 그려진다.

<img src="https://github.com/user-attachments/assets/579f1610-4831-46dd-be47-7e948022652e" width=300/>

---

### 📌 Scaffold
🔗 [ScaffoldDemo.kt](https://github.com/DoTheBestMayB/ComposeStudy/blob/master/app/src/main/java/com/dothebestmayb/composestudy/basic_layout/ScaffoldDemo.kt)

- Scaffold는 content block에 paddingValue를 제공하는데, 이것은 화면이 status bar와 겹치지 않도록 제공되는 padding 값이다.
- topBar, floatingActionButton, snackbarHost, bottomBar를 slot API 형태로 설정할 수 있다.

---

### 📌 ConstraintLayout

Compose에서는 View와 달리 hierarchy depth가 문제 되지 않는다. Preview가 없으면 composable이 어떻게 생겼는지 코드만 보고는 예측하기 어려우므로, 가능하면 Box, Column, Row 등을 사용하고 필요한 경우에만 ConstraintLayout을 사용하자.

---

### 📌 WindowSizeClass
🔗 [WindowSizeClassDemo.kt](https://github.com/DoTheBestMayB/ComposeStudy/blob/master/app/src/main/java/com/dothebestmayb/composestudy/basic_layout/WindowSizeClassDemo.kt)
🔗 [HotelBookingScreen.kt](https://github.com/DoTheBestMayB/ComposeStudy/blob/master/app/src/main/java/com/dothebestmayb/composestudy/basic_layout/HotelBookingScreen.kt)


기기의 화면 크기에 따라 다른 UI를 표시할 때 사용한다.
`Width` 기준: Compact (~600dp) → Medium (~840dp) → Expanded  
`Height` 기준: Compact (~480dp) → Medium (~900dp) → Expanded


**작은 화면(Compact)**
<img src="https://github.com/user-attachments/assets/d99ac0e0-8aef-44b5-96fd-d2a5457f5917" width=300/>

**큰 화면(Expanded)**
<img src="https://github.com/user-attachments/assets/4ab16513-e3a0-4cd0-a742-44491c6a0a21" width=300/>

---

# 🚀 배운 내용 응용해서 구현하기

## 🗂️ basic_modifier

🔗 [WipeToReply.kt](https://github.com/DoTheBestMayB/ComposeStudy/blob/master/app/src/main/java/com/dothebestmayb/composestudy/basic_modifier/self_study/WipeToReply.kt)

`Focus`, `AnchoredDraggable`, 그리고 `Scroll`을 활용하여 **채팅방 답장 기능**을 구현한 파일입니다.

### 🎥 구현 미리보기
<div align="center">
  <img src="https://github.com/user-attachments/assets/db95dd93-06e1-40b8-af5b-fb9a3f0c1cae" alt="채팅방 답장 기능 구현 미리보기" width="300" />
</div>
