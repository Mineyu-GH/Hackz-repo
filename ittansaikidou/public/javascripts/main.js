

async function submitWord() {
    const input = document.getElementById('wordInput');
    const word = input.value.trim();

    if (!word) return;

    try {
        const response = await fetch('/submit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Csrf-Token': csrfToken
            },
            body: JSON.stringify({word: word})
        });

        const data = await response.json();

        if (response.ok) {
            updateGameState(data.game);
            updateTimer(data.timeRemaining);
            input.value = '';
            document.getElementById('error').textContent = '';

            // Start timer update interval
            startTimerUpdate();
        } else {
            document.getElementById('error').textContent = data.error;
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

async function newGame() {
    try {
        const response = await fetch('/newGame', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Csrf-Token': csrfToken
            }
        });
        const data = await response.json();
        updateGameState(data);
        document.getElementById('error').textContent = '';
        document.getElementById('wordInput').value = '';
        document.getElementById('wordInput').disabled = false;
        document.getElementById('timer').textContent = 'Time Remaining: 30s';

        // Stop timer update if it's running
        stopTimerUpdate();
    } catch (error) {
        console.error('Error:', error);
    }
}

function updateGameState(game) {
    document.getElementById('targetChar').textContent = game.targetChar;
    document.getElementById('score').textContent = game.score;
    document.getElementById('gameState').textContent = game.state;
    document.getElementById('lastWord').textContent = game.lastword;

    const wordList = document.getElementById('usedWords');
    wordList.innerHTML = '';
    game.usedWords.forEach(word => {
        const li = document.createElement('li');
        li.textContent = word;
        wordList.appendChild(li);
    });

    if (game.state === 'GameOver') {
        document.getElementById('wordInput').disabled = true;
        stopTimerUpdate();
    }
}

// Timer related functions
let timerInterval;

function startTimerUpdate() {
    if (timerInterval) {
        clearInterval(timerInterval);
    }
    timerInterval = setInterval(updateTimerDisplay, 1000);
}

function stopTimerUpdate() {
    if (timerInterval) {
        clearInterval(timerInterval);
        timerInterval = null;
    }
}

async function updateTimerDisplay() {
    try {
        const response = await fetch('/getTimeRemaining');
        const data = await response.json();
        updateTimer(data.timeRemaining);
        updateProgressbar(data.timeRemaining)

        if (data.timeRemaining <= 0) {
            stopTimerUpdate();
            document.getElementById('wordInput').disabled = true;
            alert('Time\'s up! Game Over!');
        }
    } catch (error) {
        console.error('Error updating timer:', error);
    }
}

function updateTimer(timeRemaining) {
    document.getElementById('timer').textContent = `Time Remaining: ${timeRemaining}s`;
}

function updateProgressbar(timeRemaining) {
    var progress = timerInterval - timeRemaining;
    bar.animate(progress);
}

// 以下フロントで使用するライブラリの適用
//プログレスバー
var bar = new ProgressBar.Line(container, {
    strokeWidth: 4,
    easing: 'linear',
    duration: timerInterval,
    color: '#FFEA82',
    trailColor: '#eee',
    trailWidth: 1,
    svgStyle: {width: '100%', height: '100%'},
    from: {color: '#FFEA82'},
    to: {color: '#ED6A5A'},
    step: (state, bar) => {
        bar.path.setAttribute('stroke', state.color);
    }
});
bar.set(1.0); // 初期状態を100%に設定

