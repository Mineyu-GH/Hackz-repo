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
            body: JSON.stringify({ word: word })
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

// Add event listeners
document.addEventListener('DOMContentLoaded', function() {
    const wordInput = document.getElementById('wordInput');
    if (wordInput) {
        wordInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                submitWord();
            }
        });
    }
});
