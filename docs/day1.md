## 깃헙 작업

1. remote repository에 브랜치 생성
    - 브랜치 생성시 폴더 내에 브랜치를 생성하도록 설정
        - And01A/main - 메인 브랜치
        - And01A/init-merge - 병합 처리 브랜치
2. 생성한 And01A/main 브랜치를 IDE에 단 하나의 브랜치만을 클론
    - 레포지토리 전체를 클론하면 IDE에서 모든 remote branches 에 접근이 가능하며 의도치 않은 삭제가 일어날 수 있다. (K021: 실제로 And02B 브랜치를 삭제해 버렸다.)
    - 클론 터미널 명령어

        ```bash
        git clone -b And01A/main --single-branch https://토큰번호@github.com/remote-repo-name.git
        ```

3. IDE에 추가적인 remote branch 추가
    - 클론할 때 싱글 브랜치만 클론했기에 추가적인 브랜치를 IDE의 remote에 설정하기 위한 과정이 필요
    - 추가 remote branch 설정 명령어

        ```bash
        git fetch origin And01A/init-merge:refs/remotes/origin/And01A/init-merge
        ```