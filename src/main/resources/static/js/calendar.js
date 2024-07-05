// JavaScript 코드

// 페이지가 로드되면 실행될 함수
document.addEventListener('DOMContentLoaded', function() {
    // 임시로 설정한 변수로 각 칸에 숫자를 할당합니다 (실제로는 서버에서 데이터를 받아와야 합니다)
    const daysInMonth = 30; // 예시로 30일까지 있는 달을 가정합니다
    const highlightDays = [15, 16, 17, 18, 19]; // 하이라이트할 날짜들
    const maxChecks = 3; // 사용자가 체크할 수 있는 최대 칸 수

    // 모든 달력의 div 요소들을 선택합니다
    const calendarCells = document.querySelectorAll('.calendar > div:not(.header)');

    // 클릭한 칸 수를 저장할 변수
    let checkedCount = 0;

    // 클릭 이벤트 리스너를 정의합니다
    function handleClick(event) {
        const cell = event.currentTarget;

        // .highlight 클래스가 적용된 칸만 클릭할 수 있도록 합니다
        if (!cell.classList.contains('highlight')) {
            return;
        }

        // 이미 체크된 칸이면 체크 표시를 제거합니다
        if (cell.classList.contains('checked')) {
            cell.classList.remove('checked');
            // 체크 아이콘도 제거합니다
            const icon = cell.querySelector('.check-icon');
            if (icon) {
                icon.remove();
            }
            checkedCount--;
            return;
        }

        // 현재 체크된 아이콘 개수를 확인합니다
        const currentCheckedIcons = cell.querySelectorAll('.check-icon');

        // 현재 체크된 아이콘의 개수가 최대 개수를 초과하면 클릭 이벤트를 처리하지 않습니다
        if (currentCheckedIcons.length >= maxChecks) {
            return;
        }

        // 현재 칸에 체크를 추가합니다
        cell.classList.add('checked');
        checkedCount++;

        // 체크 아이콘을 생성하고 추가합니다
        const icon = document.createElement('div');
        icon.classList.add('check-icon', 'visible');
        icon.innerHTML = '✔';
        cell.appendChild(icon);

        // 최대 체크 가능한 칸 수를 초과하면 모든 클릭 이벤트를 제거합니다
        if (checkedCount >= maxChecks) {
            calendarCells.forEach(function(cell) {
                if (!cell.classList.contains('checked')) {
                    cell.removeEventListener('click', handleClick);
                }
            });
        }
    }

    // 각 div 요소에 숫자를 할당하고 클릭 이벤트를 추가합니다
    calendarCells.forEach(function(cell, index) {
        // 현재 날짜를 구합니다 (실제로는 서버에서 데이터를 받아와야 합니다)
        const day = index + 1;

        // 각 칸에 숫자를 표시합니다
        cell.textContent = day;

        // 특정 날짜에 .highlight 클래스를 추가합니다
        if (highlightDays.includes(day)) {
            cell.classList.add('highlight');
        }

        // 클릭 이벤트 리스너를 추가합니다
        cell.addEventListener('click', handleClick);
    });
});
