const axios = require('axios');
const readline = require('readline');
const BASE_URL = 'http://localhost:8080/api';

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

function input(prompt) {
    return new Promise(resolve => rl.question(prompt, resolve));
}

function clearScreen() {
    console.clear();
}

class ClienteHotelaria {
    constructor(baseUrl) {
        this.baseUrl = baseUrl;
    }

    async listarQuartosDisponiveis() {
        try {
            const res = await axios.get(`${this.baseUrl}/quartos/disponiveis`);
            const quartos = res.data;
            console.log("\n--- Quartos Disponíveis ---");
            if (quartos.length > 0) {
                quartos.forEach(q => {
                    console.log(`  Número: ${q.numero}, Tipo: ${q.tipo}, Preço: R$${q.precoDiaria.toFixed(2)}, Capacidade: ${q.capacidade}`);
                });
            } else {
                console.log("  Nenhum quarto disponível no momento.");
            }
        } catch (err) {
            console.error("Erro ao listar quartos disponíveis:", err.message);
        }
    }

    async adicionarQuarto(numero, tipo, capacidade, precoDiaria, disponivel) {
        try {
            const res = await axios.post(`${this.baseUrl}/quartos`, {
                numero, tipo, capacidade, precoDiaria, disponivel
            });
            console.log(`Quarto ${numero} adicionado com sucesso!`);
        } catch (err) {
            if (err.response?.status === 409) {
                console.error(`Erro: Já existe um quarto com o número ${numero}.`);
            } else {
                console.error("Erro ao adicionar quarto:", err.message);
            }
        }
    }

    async buscarQuarto(numero) {
        try {
            const res = await axios.get(`${this.baseUrl}/quartos/${numero}`);
            const q = res.data;
            console.log(`\n--- Quarto Encontrado ---\n  Número: ${q.numero}, Tipo: ${q.tipo}, Preço: R$${q.precoDiaria.toFixed(2)}, Disponível: ${q.disponivel}`);
        } catch (err) {
            console.error("Erro ao buscar quarto:", err.response?.status === 404 ? `Quarto ${numero} não encontrado.` : err.message);
        }
    }

    async atualizarDisponibilidadeQuarto(numero, disponivel) {
        try {
            await axios.put(`${this.baseUrl}/quartos/${numero}/disponibilidade?disponivel=${disponivel}`);
            console.log(`Disponibilidade do Quarto ${numero} atualizada para ${disponivel}.`);
        } catch (err) {
            console.error("Erro ao atualizar disponibilidade:", err.message);
        }
    }

    async removerQuarto(numero) {
        try {
            await axios.delete(`${this.baseUrl}/quartos/${numero}`);
            console.log(`Quarto ${numero} removido com sucesso.`);
        } catch (err) {
            console.error("Erro ao remover quarto:", err.response?.status === 404 ? `Quarto ${numero} não encontrado.` : err.message);
        }
    }

    async fazerReserva(numeroQuarto, cpfHospede, nomeHospede, dataCheckin, dataCheckout) {
        try {
            const res = await axios.post(`${this.baseUrl}/reservas`, {
                numeroQuarto,
                hospede: { cpf: cpfHospede, nome: nomeHospede },
                dataCheckin,
                dataCheckout
            });
            console.log(`Reserva realizada com sucesso! ID: ${res.data.idReserva}`);
        } catch (err) {
            console.error("Erro ao fazer reserva:", err.response?.data || err.message);
        }
    }

    async cancelarReserva(id) {
        try {
            await axios.delete(`${this.baseUrl}/reservas/${id}`);
            console.log(`Reserva ${id} cancelada com sucesso.`);
        } catch (err) {
            console.error("Erro ao cancelar reserva:", err.response?.status === 404 ? `Reserva ${id} não encontrada.` : err.message);
        }
    }

    async buscarReserva(id) {
        try {
            const res = await axios.get(`${this.baseUrl}/reservas/${id}`);
            const r = res.data;
            console.log(`\n--- Reserva Encontrada ---\n  ID: ${r.idReserva}, Quarto: ${r.numeroQuarto}, Hóspede: ${r.hospede.nome}, Check-in: ${r.dataCheckin}, Check-out: ${r.dataCheckout}`);
        } catch (err) {
            console.error("Erro ao buscar reserva:", err.response?.status === 404 ? `Reserva ${id} não encontrada.` : err.message);
        }
    }

    async listarReservasPorHospede(cpf) {
        try {
            const res = await axios.get(`${this.baseUrl}/reservas/hospede/${cpf}`);
            const reservas = res.data;
            console.log(`\n--- Reservas para CPF ${cpf} ---`);
            if (reservas.length > 0) {
                reservas.forEach(r => {
                    console.log(`  ID: ${r.idReserva}, Quarto: ${r.numeroQuarto}, Check-in: ${r.dataCheckin}, Check-out: ${r.dataCheckout}`);
                });
            } else {
                console.log("  Nenhuma reserva encontrada.");
            }
        } catch (err) {
            console.error("Erro ao listar reservas:", err.message);
        }
    }

    async listarTodasReservas() {
        try {
            const res = await axios.get(`${this.baseUrl}/reservas`);
            const reservas = res.data;
            console.log("\n--- Todas as Reservas ---");
            reservas.forEach(r => {
                console.log(`  ID: ${r.idReserva}, Quarto: ${r.numeroQuarto}, Hóspede: ${r.hospede.nome}, Check-in: ${r.dataCheckin}, Check-out: ${r.dataCheckout}`);
            });
        } catch (err) {
            console.error("Erro ao listar reservas:", err.message);
        }
    }
}

// --- Menus ---

async function menuHospede(cliente) {
    while (true) {
        clearScreen();
        console.log("\n--- MENU DO HÓSPEDE ---");
        console.log("1. Listar Quartos Disponíveis");
        console.log("2. Fazer Reserva");
        console.log("3. Listar Minhas Reservas");
        console.log("4. Voltar");

        const op = await input("Escolha uma opção: ");
        if (op === '1') await cliente.listarQuartosDisponiveis();
        else if (op === '2') {
            const numero = parseInt(await input("Número do Quarto: "));
            const cpf = await input("Seu CPF: ");
            const nome = await input("Seu Nome: ");
            const checkin = await input("Check-in (AAAA-MM-DD): ");
            const checkout = await input("Check-out (AAAA-MM-DD): ");
            await cliente.fazerReserva(numero, cpf, nome, checkin, checkout);
        } else if (op === '3') {
            const cpf = await input("Digite seu CPF: ");
            await cliente.listarReservasPorHospede(cpf);
        } else if (op === '4') break;
        else console.log("Opção inválida.");
        await input("\nPressione Enter para continuar...");
    }
}

async function menuFuncionario(cliente) {
    while (true) {
        clearScreen();
        console.log("\n--- MENU DO FUNCIONÁRIO ---");
        console.log("1. Gerenciar Quartos");
        console.log("2. Gerenciar Reservas");
        console.log("3. Voltar");

        const op = await input("Escolha uma opção: ");
        if (op === '1') await menuQuartos(cliente);
        else if (op === '2') await menuReservas(cliente);
        else if (op === '3') break;
        else console.log("Opção inválida.");
        await input("\nPressione Enter para continuar...");
    }
}

async function menuQuartos(cliente) {
    while (true) {
        clearScreen();
        console.log("\n--- GERENCIAR QUARTOS ---");
        console.log("1. Listar Quartos");
        console.log("2. Adicionar Quarto");
        console.log("3. Remover Quarto");
        console.log("4. Buscar Quarto");
        console.log("5. Atualizar Disponibilidade");
        console.log("6. Voltar");

        const op = await input("Escolha: ");
        if (op === '1') await cliente.listarQuartosDisponiveis();
        else if (op === '2') {
            const numero = parseInt(await input("Número: "));
            const tipo = await input("Tipo: ");
            const capacidade = parseInt(await input("Capacidade: "));
            const preco = parseFloat(await input("Preço da Diária: "));
            const disp = await input("Disponível (s/n): ");
            await cliente.adicionarQuarto(numero, tipo, capacidade, preco, disp.toLowerCase() === 's');
        } else if (op === '3') {
            const numero = parseInt(await input("Número do Quarto: "));
            await cliente.removerQuarto(numero);
        } else if (op === '4') {
            const numero = parseInt(await input("Número do Quarto: "));
            await cliente.buscarQuarto(numero);
        } else if (op === '5') {
            const numero = parseInt(await input("Número: "));
            const disp = await input("Disponível (s/n): ");
            await cliente.atualizarDisponibilidadeQuarto(numero, disp.toLowerCase() === 's');
        } else if (op === '6') break;
        else console.log("Opção inválida.");
        await input("\nPressione Enter para continuar...");
    }
}

async function menuReservas(cliente) {
    while (true) {
        clearScreen();
        console.log("\n--- GERENCIAR RESERVAS ---");
        console.log("1. Listar Todas");
        console.log("2. Nova Reserva");
        console.log("3. Cancelar Reserva");
        console.log("4. Buscar por ID");
        console.log("5. Listar por CPF");
        console.log("6. Voltar");

        const op = await input("Escolha: ");
        if (op === '1') await cliente.listarTodasReservas();
        else if (op === '2') {
            const numero = parseInt(await input("Número do Quarto: "));
            const cpf = await input("CPF: ");
            const nome = await input("Nome: ");
            const checkin = await input("Check-in (AAAA-MM-DD): ");
            const checkout = await input("Check-out (AAAA-MM-DD): ");
            await cliente.fazerReserva(numero, cpf, nome, checkin, checkout);
        } else if (op === '3') {
            const id = await input("ID da Reserva: ");
            await cliente.cancelarReserva(id);
        } else if (op === '4') {
            const id = await input("ID da Reserva: ");
            await cliente.buscarReserva(id);
        } else if (op === '5') {
            const cpf = await input("CPF: ");
            await cliente.listarReservasPorHospede(cpf);
        } else if (op === '6') break;
        else console.log("Opção inválida.");
        await input("\nPressione Enter para continuar...");
    }
}

// --- Menu Principal ---

async function main() {
    const cliente = new ClienteHotelaria(BASE_URL);
    while (true) {
        clearScreen();
        console.log("--- SISTEMA DE HOTELARIA ---");
        console.log("1. Acessar como Hóspede");
        console.log("2. Acessar como Funcionário");
        console.log("3. Sair");

        const op = await input("Escolha: ");
        if (op === '1') await menuHospede(cliente);
        else if (op === '2') await menuFuncionario(cliente);
        else if (op === '3') {
            console.log("Saindo do sistema. Obrigado!");
            rl.close();
            break;
        } else {
            console.log("Opção inválida.");
            await input("\nPressione Enter para continuar...");
        }
    }
}

main();
