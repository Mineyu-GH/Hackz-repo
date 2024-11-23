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
            updateGameState(data);
            input.value = '';
            document.getElementById('error').textContent = '';
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
        }
}
