package br.edu.ifsp.spo.todolist.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

record Tarefa(String texto, boolean concluida) { }

@Controller
public class TarefasController {

    private List<Tarefa> tarefas = new ArrayList<>(Arrays.asList(
            new Tarefa("Comprar pão", false), // Concluídas
            new Tarefa("Comprar batata", true), // Pendente
            new Tarefa("Estudar para a prova de SPOLBP2", false) // Concluídas
    ));

    // Método para listar tarefas com base no filtro
    @GetMapping("/tarefas")
    public String listarTarefas(@RequestParam(value = "filtro", required = false, defaultValue = "todas") String filtro,
                                Model model) {
        List<Tarefa> tarefasFiltradas = new ArrayList<>();

        // Filtrando as tarefas conforme o filtro
        if (filtro.equals("todas")) {
            tarefasFiltradas = tarefas; // Exibe todas as tarefas
        } else if (filtro.equals("concluidas")) {
            // Exibe as tarefas **não concluídas** (concluida == false)
            for (Tarefa tarefa : tarefas) {
                if (tarefa.concluida()) {
                    tarefasFiltradas.add(tarefa);
                }
            }
        } else if (filtro.equals("pendentes")) {
            // Exibe as tarefas **concluídas** (concluida == true)
            for (Tarefa tarefa : tarefas) {
                if (!tarefa.concluida()) {
                    tarefasFiltradas.add(tarefa);
                }
            }
        }

        model.addAttribute("tarefas", tarefasFiltradas);
        model.addAttribute("filtroAtual", filtro);

        return "tarefas/index"; // Nome da view
    }

    // Método para concluir uma tarefa
    @GetMapping("/tarefas/concluir/{texto}")
    public String concluirTarefa(@PathVariable String texto) {
        // Atualizando a tarefa para concluída
        for (int i = 0; i < tarefas.size(); i++) {
            if (tarefas.get(i).texto().equalsIgnoreCase(texto)) {
                tarefas.set(i, new Tarefa(tarefas.get(i).texto(), true)); // Marca como concluída
                break;
            }
        }
        return "redirect:/tarefas"; // Redireciona de volta para a lista de tarefas
    }
}