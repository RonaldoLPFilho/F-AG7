const axios = require('axios');

console.clear(); 
console.log('[Pedidos Service] Iniciando...');

function gerarSufixoAleatorio() {
  const timestamp = Date.now();
  const sufixo = Math.floor(Math.random() * 10000);
  return `${timestamp}${sufixo}`;
}

async function aguardarAuthService() {
  const url = 'http://auth-service:8080/health-check';

  console.log('[Pedidos Service] Aguardando Auth Service estar pronto...');

  for (let tentativas = 0; tentativas < 10; tentativas++) {
    try {
      const response = await axios.get(url);
      if (response.status === 200) {
        console.log('[Pedidos Service] Auth Service está disponível!');
        return true;
      }
    } catch (err) {
      console.log(`[Pedidos Service] Tentativa ${tentativas + 1}: Auth Service ainda não está pronto...`);
      console.log(err);
      await new Promise(res => setTimeout(res, 3000)); 
    }
  }

  console.error('[Pedidos Service] Timeout esperando Auth Service. Encerrando.');
  process.exit(1);
}

async function criarContaEExibirToken() {
  const sufixo = gerarSufixoAleatorio();

  const payload = {
    username: `pedido_user_${sufixo}`,
    password: 'senha123',
    email: `pedido_${sufixo}@vinheria.com`,
    fullName: 'Usuário Pedidos',
    role: 'USER'
  };

  try {
    const response = await axios.post('http://auth-service:8080/api/auth/register', payload);

    if (response.status === 200 || response.status === 201) {
      console.log('[Pedidos Service] Usuário registrado com sucesso!');
      console.log('[Pedidos Service] JWT recebido:');
      console.log(response.data.data.token);
    } else {
      console.log(`[Pedidos Service] Status inesperado: ${response.status}`);
    }
  } catch (error) {
    console.error('❌ [Pedidos Service] Erro ao registrar usuário:', error.response?.data || error.message);
  }
}

(async () => {
  await aguardarAuthService();
  await criarContaEExibirToken();
})();
