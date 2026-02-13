//Detecta se está rodando no seu PC ou na Internet
const isLocal = window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1";

//URL principal para conectar com o Backend
const API_URL = isLocal 
    ? "http://localhost:9090/api" 
    : "https://teste-mrh-backend.onrender.com/api";

//Verifica qual elemento existe na tela para rodar a função certa 
window.onload = function () {

    if (document.getElementById("formUpload")) {
        configurarUpload();
    }

    if (document.getElementById("documentosTabela")) {
        carregarDocumentos();
    }

    if (document.getElementById("documentPreview")) {
        carregarDetalhes();
    }
};


//Função de upload de arquivos
function configurarUpload() {
    const form = document.getElementById("formUpload");

    form.addEventListener("submit", async function (e) {
        e.preventDefault();

        //Cria o pacote de dados para enviar (Multipart)
        const formData = new FormData();
        formData.append("titulo", document.getElementById("titulo").value);
        formData.append("descricao", document.getElementById("descricao").value);

        const arquivoInput = document.getElementById("arquivo");
        if (arquivoInput.files.length > 0) {
            formData.append("arquivo", arquivoInput.files[0]);
        }

        //Envia os dados para o backend
        try {
            const resposta = await fetch(API_URL + "/documentos", {
                method: "POST",
                body: formData
            });

            if (resposta.ok) {
                alert("Documento enviado com sucesso!");
                window.location.href = "documentos.html";
            } else {
                alert("Erro ao enviar o documento.");
            }
        } catch (erro) {
            console.error("Erro no upload:", erro);
            alert("Erro de conexão com o servidor.");
        }
    });
}


//Função para listar os documentos
async function carregarDocumentos() {
    const tabela = document.getElementById("documentosTabela");
    tabela.innerHTML = "<tr><td colspan='4' style='text-align:center'>Carregando...</td></tr>";

    try {
        const resposta = await fetch(API_URL + "/documentos");
        const documentos = await resposta.json();

        tabela.innerHTML = ""; //Limpa o "Carregando..."

        if (documentos.length === 0) {
            tabela.innerHTML = "<tr><td colspan='4' style='text-align:center'>Nenhum documento encontrado.</td></tr>";
            return;
        }

        //Para ordenar do mais recente para o mais antigo
        documentos.sort((a, b) => new Date(b.dataUpload) - new Date(a.dataUpload));

        //Cria as linhas da tabela
        documentos.forEach(function (doc) {
            //Formata a data para o padrão brasileiro
            const dataFormatada = new Date(doc.dataUpload).toLocaleDateString("pt-BR");

            tabela.innerHTML += `
                <tr>
                    <td>${doc.titulo}</td>
                    <td>${dataFormatada}</td>
                    <td>${doc.descricao || "-"}</td>
                    <td>
                        <div class="acoes">
                            <a href="detalhes.html?id=${doc.id}" class="btn-padrao">Visualizar</a>
                            <a href="${API_URL}/documentos/${doc.id}/download" class="btn-padrao" id="btn-download">Baixar</a>
                        </div>
                    </td>
                </tr>
            `;
        });

    } catch (erro) {
        console.error("Erro ao listar:", erro);
        tabela.innerHTML = "<tr><td colspan='4' style='text-align:center'>Erro ao conectar na API.</td></tr>";
    }
}


//Funções para carregas os dados de talhes
async function carregarDetalhes() {
    //Pega o ID que está na URL (ex: detalhes.html?id=1)
    const parametros = new URLSearchParams(window.location.search);
    const id = parametros.get("id");

    //Verifica se o ID não é vazio
    if (!id) {
        alert("Documento não identificado.");
        window.location.href = "documentos.html";
        return;
    }

    try {
        //Busca todos os documentos e encontra o correto pelo ID
        const resposta = await fetch(API_URL + "/documentos");
        const documentos = await resposta.json();

        const doc = documentos.find(d => d.id == id);

        if (!doc) {
            document.getElementById("documentInfo").innerHTML = "Documento não encontrado.";
            return;
        }

        //Preenche as informações na tela
        const dataFormatada = new Date(doc.dataUpload).toLocaleString("pt-BR");
        document.getElementById("documentInfo").innerHTML = `
            <h2>${doc.titulo}</h2>
            <p><strong>Descrição:</strong> ${doc.descricao || "Sem descrição"}</p>
            <p><strong>Arquivo:</strong> ${doc.nomeArquivo}</p>
            <p><strong>Enviado em:</strong> ${dataFormatada}</p>
        `;

        //Carrega o Preview e os Comentários
        carregarPreview(doc.id, doc.nomeArquivo);
        carregarComentarios(id);

        //Ativa o botão de enviar novo comentário
        document.getElementById("enviarComentario").onclick = function () {
            enviarComentario(id);
        };

    } catch (erro) {
        console.error("Erro nos detalhes:", erro);
        document.getElementById("documentInfo").innerHTML = "Erro ao carregar dados.";
    }
}


//Função de visualizar o documento, verificando o tipo de arquivo para mostrar na tela
async function carregarPreview(id, nomeArquivo) {
    const previewDiv = document.getElementById("documentPreview");
    previewDiv.innerHTML = "Carregando visualização...";

    //Descobre a extensão do arquivo (pdf, png, jpg...)
    const extensao = nomeArquivo.split(".").pop().toLowerCase();
    const urlDownload = `${API_URL}/documentos/${id}/download`;

    try {
        //Para conseguir exibir o arquivo na tela
        const resposta = await fetch(urlDownload);
        const blob = await resposta.blob();
        const blobUrl = URL.createObjectURL(blob);

        previewDiv.innerHTML = "";

        //Valida o tipo do arquivo
        if (extensao === "pdf") {
            //Se for PDF, cria um iframe
            previewDiv.innerHTML = `<iframe src="${blobUrl}"></iframe>`;
        } 
        else if (["jpg", "jpeg", "png", "gif", "webp"].includes(extensao)) {
            //Se for imagem, cria uma tag img
            previewDiv.innerHTML = `<img src="${blobUrl}" alt="Visualização do documento">`;
        } 
        else {
            //Para outros tipos (ex: zip, docx)
            previewDiv.innerHTML = `
                <div style="text-align:center; color:#DAA520;">
                    <p>Visualização indisponível para este formato.</p>
                    <a href="${urlDownload}" class="btn-padrao">Baixar Arquivo</a>
                </div>
            `;
        }

    } catch (erro) {
        console.error("Erro no preview:", erro);
        previewDiv.innerHTML = "Não foi possível carregar a visualização.";
    }
}


//Função para carregar os comentários
async function carregarComentarios(id) {
    const lista = document.getElementById("listaComentarios");

    try {
        const resposta = await fetch(`${API_URL}/documentos/${id}/comentarios`);
        const comentarios = await resposta.json();

        lista.innerHTML = "";

        if (comentarios.length === 0) {
            lista.innerHTML = "<p style='color:#ccc; text-align:center'>Nenhum comentário ainda.</p>";
            return;
        }

        comentarios.forEach(function (c) {
            const data = new Date(c.dataHoraComentario).toLocaleString("pt-BR");

            lista.innerHTML += `
                <div style="border-bottom:1px solid #333; padding:10px 0;">
                    <small style="color:#DAA520;">
                        ${data}
                    </small>
                    <div>${c.comentario}</div>
                </div>
            `;
        });
        
        //Mantém a rolagem no final da lista
        lista.scrollTop = lista.scrollHeight;

    } catch (erro) {
        console.error("Erro nos comentários:", erro);
        lista.innerHTML = "Erro ao carregar comentários.";
    }
}


//Função para enviar um novo comentário
async function enviarComentario(id) {
    const campoTexto = document.getElementById("novoComentario");
    const texto = campoTexto.value;

    if (!texto.trim()) {
        alert("Por favor, digite um comentário.");
        return;
    }

    try {
        const resposta = await fetch(`${API_URL}/documentos/${id}/comentarios`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ comentario: texto })
        });

        if (resposta.ok) {
            campoTexto.value = "";
            //Para recarregar os comentários e mostrar o novo sem precisar atualizar a página
            carregarComentarios(id);
        } else {
            alert("Não foi possível salvar o comentário.");
        }

    } catch (erro) {
        console.error("Erro ao enviar:", erro);
        alert("Erro de conexão.");
    }
}