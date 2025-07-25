import requests
import json
import os # Para limpar a tela

BASE_URL = "http://localhost:8080/api"

class ClienteHotelaria:
    def __init__(self, base_url):
        self.base_url = base_url

    # --- Métodos para Quarto (mantidos iguais, mas serão chamados do menu) ---

    def listar_quartos_disponiveis(self):
        url = f"{self.base_url}/quartos/disponiveis"
        try:
            response = requests.get(url)
            response.raise_for_status()
            quartos = response.json()
            print("\n--- Quartos Disponíveis ---")
            if quartos:
                for quarto in quartos:
                    print(f"  Número: {quarto['numero']}, Tipo: {quarto['tipo']}, Preço: R${quarto['precoDiaria']:.2f}, Capacidade: {quarto['capacidade']}")
            else:
                print("  Nenhum quarto disponível no momento.")
            return quartos
        except requests.exceptions.RequestException as e:
            print(f"Erro ao listar quartos disponíveis: {e}")
            return None

    def adicionar_quarto(self, numero, tipo, capacidade, precoDiaria, disponivel):
        url = f"{self.base_url}/quartos"
        quarto_data = {
            "numero": numero,
            "tipo": tipo,
            "capacidade": capacidade,
            "precoDiaria": precoDiaria,
            "disponivel": disponivel
        }
        try:
            response = requests.post(url, json=quarto_data)
            response.raise_for_status()
            if response.status_code == 201:
                print(f"Quarto {numero} adicionado com sucesso!")
                return response.json()
            else:
                print(f"Erro ao adicionar quarto {numero}. Status: {response.status_code}, Resposta: {response.text}")
                return None
        except requests.exceptions.HTTPError as e:
            if e.response.status_code == 409:
                print(f"Erro: Já existe um quarto com o número {numero}.")
            else:
                print(f"Erro HTTP ao adicionar quarto: {e}")
            return None
        except requests.exceptions.RequestException as e:
            print(f"Erro de requisição ao adicionar quarto: {e}")
            return None

    def buscar_quarto(self, numero):
        url = f"{self.base_url}/quartos/{numero}"
        try:
            response = requests.get(url)
            response.raise_for_status()
            quarto = response.json()
            print(f"\n--- Quarto Encontrado ---")
            print(f"  Número: {quarto['numero']}, Tipo: {quarto['tipo']}, Preço: R${quarto['precoDiaria']:.2f}, Disponível: {quarto['disponivel']}")
            return quarto
        except requests.exceptions.HTTPError as e:
            if e.response.status_code == 404:
                print(f"Quarto {numero} não encontrado.")
            else:
                print(f"Erro HTTP ao buscar quarto: {e}")
            return None
        except requests.exceptions.RequestException as e:
            print(f"Erro de requisição ao buscar quarto: {e}")
            return None

    def atualizar_disponibilidade_quarto(self, numero, disponivel):
        url = f"{self.base_url}/quartos/{numero}/disponibilidade?disponivel={str(disponivel).lower()}"
        try:
            response = requests.put(url)
            response.raise_for_status()
            if response.status_code == 200:
                print(f"Disponibilidade do Quarto {numero} atualizada para {disponivel}.")
                return True
            else:
                print(f"Erro ao atualizar disponibilidade. Status: {response.status_code}, Resposta: {response.text}")
                return False
        except requests.exceptions.HTTPError as e:
            if e.response.status_code == 404:
                print(f"Quarto {numero} não encontrado para atualizar disponibilidade.")
            else:
                print(f"Erro HTTP ao atualizar disponibilidade: {e}")
            return False
        except requests.exceptions.RequestException as e:
            print(f"Erro de requisição ao atualizar disponibilidade: {e}")
            return False

    def remover_quarto(self, numero):
        url = f"{self.base_url}/quartos/{numero}"
        try:
            response = requests.delete(url)
            response.raise_for_status()
            if response.status_code == 204:
                print(f"Quarto {numero} removido com sucesso.")
                return True
            else:
                print(f"Erro ao remover quarto {numero}. Status: {response.status_code}, Resposta: {response.text}")
                return False
        except requests.exceptions.HTTPError as e:
            if e.response.status_code == 404:
                print(f"Quarto {numero} não encontrado para remoção.")
            else:
                print(f"Erro HTTP ao remover quarto: {e}")
            return False
        except requests.exceptions.RequestException as e:
            print(f"Erro de requisição ao remover quarto: {e}")
            return False

    # --- Métodos para Reserva (mantidos iguais, mas serão chamados do menu) ---

    def fazer_reserva(self, numero_quarto, cpf_hospede, nome_hospede, data_checkin, data_checkout):
        url = f"{self.base_url}/reservas"
        reserva_data = {
            "numeroQuarto": numero_quarto,
            "hospede": {
                "cpf": cpf_hospede,
                "nome": nome_hospede
            },
            "dataCheckin": data_checkin,
            "dataCheckout": data_checkout
        }
        try:
            response = requests.post(url, json=reserva_data)
            response.raise_for_status()
            if response.status_code == 201:
                nova_reserva = response.json()
                print(f"Reserva para o quarto {numero_quarto} realizada com sucesso! ID: {nova_reserva.get('idReserva')}")
                return nova_reserva
            else:
                print(f"Erro ao fazer reserva. Status: {response.status_code}, Resposta: {response.text}")
                return None
        except requests.exceptions.HTTPError as e:
            if e.response.status_code == 400:
                print(f"Erro: Quarto {numero_quarto} não disponível ou dados inválidos. Resposta: {e.response.text}")
            else:
                print(f"Erro HTTP ao fazer reserva: {e}")
            return None
        except requests.exceptions.RequestException as e:
            print(f"Erro de requisição ao fazer reserva: {e}")
            return None

    def cancelar_reserva(self, id_reserva):
        url = f"{self.base_url}/reservas/{id_reserva}"
        try:
            response = requests.delete(url)
            response.raise_for_status()
            if response.status_code == 204:
                print(f"Reserva {id_reserva} cancelada com sucesso.")
                return True
            else:
                print(f"Erro ao cancelar reserva. Status: {response.status_code}, Resposta: {response.text}")
                return False
        except requests.exceptions.HTTPError as e:
            if e.response.status_code == 404:
                print(f"Reserva com ID {id_reserva} não encontrada para cancelamento.")
            else:
                print(f"Erro HTTP ao cancelar reserva: {e}")
            return False
        except requests.exceptions.RequestException as e:
            print(f"Erro de requisição ao cancelar reserva: {e}")
            return False

    def buscar_reserva(self, id_reserva):
        url = f"{self.base_url}/reservas/{id_reserva}"
        try:
            response = requests.get(url)
            response.raise_for_status()
            reserva = response.json()
            print(f"\n--- Reserva Encontrada ---")
            print(f"  ID: {reserva.get('idReserva')}, Quarto: {reserva.get('numeroQuarto')}, Hóspede: {reserva.get('hospede', {}).get('nome')}, Check-in: {reserva.get('dataCheckin')}, Check-out: {reserva.get('dataCheckout')}")
            return reserva
        except requests.exceptions.HTTPError as e:
            if e.response.status_code == 404:
                print(f"Reserva com ID {id_reserva} não encontrada.")
            else:
                print(f"Erro HTTP ao buscar reserva: {e}")
            return None
        except requests.exceptions.RequestException as e:
            print(f"Erro de requisição ao buscar reserva: {e}")
            return None

    def listar_reservas_por_hospede(self, cpf):
        url = f"{self.base_url}/reservas/hospede/{cpf}"
        try:
            response = requests.get(url)
            response.raise_for_status()
            reservas = response.json()
            print(f"\n--- Reservas para CPF {cpf} ---")
            if reservas:
                for reserva in reservas:
                    print(f"  ID: {reserva.get('idReserva')}, Quarto: {reserva.get('numeroQuarto')}, Check-in: {reserva.get('dataCheckin')}, Check-out: {reserva.get('dataCheckout')}")
            else:
                print(f"  Nenhuma reserva encontrada para o CPF {cpf}.")
            return reservas
        except requests.exceptions.HTTPError as e:
            if e.response.status_code == 404: # A API retorna 404 para "não encontrado" mesmo para lista vazia
                print(f"  Nenhuma reserva encontrada para o CPF {cpf}.")
            else:
                print(f"Erro HTTP ao listar reservas por hóspede: {e}")
            return None
        except requests.exceptions.RequestException as e:
            print(f"Erro de requisição ao listar reservas por hóspede: {e}")
            return None

    def listar_todas_reservas(self):
        url = f"{self.base_url}/reservas"
        try:
            response = requests.get(url)
            response.raise_for_status()
            reservas = response.json()
            print("\n--- Todas as Reservas ---")
            if reservas:
                for reserva in reservas:
                    print(f"  ID: {reserva.get('idReserva')}, Quarto: {reserva.get('numeroQuarto')}, Hóspede: {reserva.get('hospede', {}).get('nome')}, Check-in: {reserva.get('dataCheckin')}, Check-out: {reserva.get('dataCheckout')}")
            else:
                print("  Nenhuma reserva encontrada.")
            return reservas
        except requests.exceptions.RequestException as e:
            print(f"Erro ao listar todas as reservas: {e}")
            return None

# Função para limpar a tela do console
def clear_screen():
    os.system('cls' if os.name == 'nt' else 'clear')

# --- Funções de Menu ---

def menu_hospede(cliente):
    while True:
        clear_screen()
        print("\n--- MENU DO HÓSPEDE ---")
        print("1. Listar Quartos Disponíveis")
        print("2. Fazer Reserva")
        print("3. Listar Minhas Reservas (por CPF)")
        print("4. Voltar ao Menu Principal")
        
        escolha = input("Escolha uma opção: ")

        if escolha == '1':
            cliente.listar_quartos_disponiveis()
        elif escolha == '2':
            print("\n--- Fazer Nova Reserva ---")
            try:
                numero_quarto = int(input("Número do Quarto: "))
                cpf_hospede = input("Seu CPF (apenas números): ")
                nome_hospede = input("Seu Nome: ")
                data_checkin = input("Data de Check-in (AAAA-MM-DD): ")
                data_checkout = input("Data de Check-out (AAAA-MM-DD): ")
                cliente.fazer_reserva(numero_quarto, cpf_hospede, nome_hospede, data_checkin, data_checkout)
            except ValueError:
                print("Entrada inválida. O número do quarto deve ser um número inteiro.")
        elif escolha == '3':
            cpf = input("Digite seu CPF para listar suas reservas: ")
            cliente.listar_reservas_por_hospede(cpf)
        elif escolha == '4':
            break
        else:
            print("Opção inválida. Tente novamente.")
        input("\nPressione Enter para continuar...") # Pausa para o usuário ler a saída

def menu_funcionario(cliente):
    while True:
        clear_screen()
        print("\n--- MENU DO FUNCIONÁRIO/GERENTE ---")
        print("1. Gerenciar Quartos")
        print("2. Gerenciar Reservas")
        print("3. Voltar ao Menu Principal")

        escolha_principal = input("Escolha uma opção: ")

        if escolha_principal == '1':
            while True:
                clear_screen()
                print("\n--- GERENCIAR QUARTOS ---")
                print("1. Listar Quartos Disponíveis")
                print("2. Adicionar Quarto")
                print("3. Remover Quarto")
                print("4. Buscar Quarto por Número")
                print("5. Atualizar Disponibilidade de Quarto")
                print("6. Voltar ao Menu Anterior")

                escolha_quarto = input("Escolha uma opção: ")

                if escolha_quarto == '1':
                    cliente.listar_quartos_disponiveis()
                elif escolha_quarto == '2':
                    print("\n--- Adicionar Novo Quarto ---")
                    try:
                        numero = int(input("Número do Quarto: "))
                        tipo = input("Tipo (Standard, Luxo, Suíte, etc.): ")
                        capacidade = int(input("Capacidade de Hóspedes: "))
                        preco = float(input("Preço da Diária: "))
                        disponivel_str = input("Disponível (s/n): ").lower()
                        disponivel = True if disponivel_str == 's' else False
                        cliente.adicionar_quarto(numero, tipo, capacidade, preco, disponivel)
                    except ValueError:
                        print("Entrada inválida. Verifique os tipos de dados (número, capacidade, preço).")
                elif escolha_quarto == '3':
                    try:
                        numero = int(input("Número do Quarto a ser removido: "))
                        cliente.remover_quarto(numero)
                    except ValueError:
                        print("Entrada inválida. O número do quarto deve ser um número inteiro.")
                elif escolha_quarto == '4':
                    try:
                        numero = int(input("Número do Quarto a ser buscado: "))
                        cliente.buscar_quarto(numero)
                    except ValueError:
                        print("Entrada inválida. O número do quarto deve ser um número inteiro.")
                elif escolha_quarto == '5':
                    try:
                        numero = int(input("Número do Quarto para atualizar disponibilidade: "))
                        disponivel_str = input("Definir como Disponível (s/n): ").lower()
                        disponivel = True if disponivel_str == 's' else False
                        cliente.atualizar_disponibilidade_quarto(numero, disponivel)
                    except ValueError:
                        print("Entrada inválida. O número do quarto deve ser um número inteiro.")
                elif escolha_quarto == '6':
                    break
                else:
                    print("Opção inválida. Tente novamente.")
                input("\nPressione Enter para continuar...")

        elif escolha_principal == '2':
            while True:
                clear_screen()
                print("\n--- GERENCIAR RESERVAS ---")
                print("1. Listar Todas as Reservas")
                print("2. Fazer Nova Reserva") # Funcionário pode fazer para hóspedes
                print("3. Cancelar Reserva")
                print("4. Buscar Reserva por ID")
                print("5. Listar Reservas por CPF do Hóspede")
                print("6. Voltar ao Menu Anterior")

                escolha_reserva = input("Escolha uma opção: ")

                if escolha_reserva == '1':
                    cliente.listar_todas_reservas()
                elif escolha_reserva == '2':
                    print("\n--- Fazer Nova Reserva (Funcionário) ---")
                    try:
                        numero_quarto = int(input("Número do Quarto: "))
                        cpf_hospede = input("CPF do Hóspede (apenas números): ")
                        nome_hospede = input("Nome do Hóspede: ")
                        data_checkin = input("Data de Check-in (AAAA-MM-DD): ")
                        data_checkout = input("Data de Check-out (AAAA-MM-DD): ")
                        cliente.fazer_reserva(numero_quarto, cpf_hospede, nome_hospede, data_checkin, data_checkout)
                    except ValueError:
                        print("Entrada inválida. O número do quarto deve ser um número inteiro.")
                elif escolha_reserva == '3':
                    id_reserva = input("ID da Reserva a ser cancelada: ")
                    cliente.cancelar_reserva(id_reserva)
                elif escolha_reserva == '4':
                    id_reserva = input("ID da Reserva a ser buscada: ")
                    cliente.buscar_reserva(id_reserva)
                elif escolha_reserva == '5':
                    cpf = input("Digite o CPF do hóspede para listar suas reservas: ")
                    cliente.listar_reservas_por_hospede(cpf)
                elif escolha_reserva == '6':
                    break
                else:
                    print("Opção inválida. Tente novamente.")
                input("\nPressione Enter para continuar...")

        elif escolha_principal == '3':
            break
        else:
            print("Opção inválida. Tente novamente.")
        input("\nPressione Enter para continuar...")

# --- Menu Principal ---
def main():
    cliente = ClienteHotelaria(BASE_URL)
    while True:
        clear_screen()
        print("--- SISTEMA DE HOTELARIA ---")
        print("1. Acessar como Hóspede")
        print("2. Acessar como Funcionário/Gerente")
        print("3. Sair")
        
        escolha = input("Escolha seu tipo de acesso: ")

        if escolha == '1':
            menu_hospede(cliente)
        elif escolha == '2':
            menu_funcionario(cliente)
        elif escolha == '3':
            print("Saindo do sistema. Obrigado!")
            break
        else:
            print("Opção inválida. Por favor, escolha 1, 2 ou 3.")
            input("\nPressione Enter para continuar...")

if __name__ == "__main__":
    main()