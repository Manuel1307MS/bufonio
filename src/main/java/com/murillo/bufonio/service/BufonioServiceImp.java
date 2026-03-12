package com.murillo.bufonio.service;

import com.murillo.bufonio.dto.ParchmentAnalysis;
import com.murillo.bufonio.model.Comment;
import com.murillo.bufonio.repository.CommentRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BufonioServiceImp implements BufonioService {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    @Transactional
    public ParchmentAnalysis generateParchmentAnalysis(List<Comment> comments) {

        String commentsText = comments.stream()
                .map(c -> "- " + c.getComment())
                .collect(Collectors.joining("\n"));

        String prompt = """
                Actúa como un analista organizacional experto en cultura de equipos y feedback laboral.
                
                Tu tarea es analizar comentarios anónimos de empleados y generar un informe breve, claro y accionable para el líder del equipo.
                
                REGLAS IMPORTANTES:
                - Sé específico y concreto; evita lenguaje corporativo genérico o vago.
                - No escribas explicaciones largas ni añadidos innecesarios.
                - Cada campo debe basarse directamente en los comentarios proporcionados.
                - Si un tema o patrón aparece pocas veces, menciónalo claramente.
                - Las acciones o consejos deben ser ejecutables directamente por un líder de equipo.
                - Si la lista de comentarios está vacía, enfócate en eso como el problema principal.
                - Devuelve todos los campos exactamente en el formato solicitado, sin texto fuera del JSON.
                - Para campos tipo enum (FREQUENCY, EMOTIONALTONE, etc.), usa únicamente las opciones indicadas en mayúsculas y sin añadidos.
                - **Mantén anonimato total:** si los comentarios incluyen nombres de personas, cargos o información identificable, reemplázalos por términos generales como "empleado" o "persona del equipo" para proteger la privacidad.
                
                ESTRUCTURA DEL INFORME (formato JSON compatible con ParchmentAnalysis):
                
                1. bufonioMessage
                Resumen breve (máximo 2–3 frases) en tono de bufón medieval, insinuando el problema principal de forma ingeniosa o irónica.
                
                2. urgencyLevel
                Debe de ser un numero del 0 al 10 segun el sig criterio:
                0: sin impacto; puede ignorarse
                1–3: leve; revisar eventualmente
                4–6: moderada; riesgo de afectar productividad o dinámica
                7–9: alta; problemas claros que requieren acción pronto
                10: crítica; problemas graves que necesitan atención inmediata  
                
                3. dominantTheme
                El problema o tema principal detectado en los comentarios, en una frase clara y específica (máx. 12 palabras).
                
                4. frequency
                Indica cuántos comentarios mencionan el problema principal usando las sig categorías:
                - NO APLICA
                - AISLADO
                - REPETIDO
                - FRECUENTE
                Debe seleccionarse **una sola opción**
                
                5. emotionalTone
                Indica el tono emocional predominante de los comentarios usando las sig categorias:
                - POSITIVO
                - NEUTRAL
                - FRUSTRACIÓN
                - CONFUSIÓN
                - DESMOTIVACIÓN
                - MIXTO
                Debe seleccionarse **una sola opción**
                
                6. riskLevel
                Indica el nivel de riesgo para el equipo si el problema continúa usando las sig categorias:
                - BAJO
                - MEDIO
                - ALTO
                Debe seleccionarse **una sola opción**
                
                7. detectedPatterns
                Describe en un solo texto corrido los patrones observados en los comentarios.
                No uses listas ni puntos separados. Debe leerse como una descripción continua de los comportamientos, confusiones o situaciones repetidas.
                
                8. potentialImpacts
                Describe en un solo texto corrido las posibles consecuencias para el equipo si el problema persiste.
                Evita exagerar y no uses listas; que sea una narrativa clara de los impactos esperados.
                
                9. monitoringIndicator
                Una métrica simple que el líder puede observar para saber si mejora la situación, redactada como una frase clara.
                
                10. bufonioAdvice
                Siempre agrega un consejo práctico y concreto para el líder relacionado con el problema principal, redactado en tono de bufón medieval irónico o juguetón, debe de ser un texto pequeño.
                
                COMENTARIOS DE EMPLEADOS:
                %s
                
                Devuelve la respuesta estrictamente en formato JSON compatible con ParchmentAnalysis.
                No incluyas texto fuera del JSON.
                """.formatted(commentsText);

        ParchmentAnalysis parchmentAnalysis = chatClient.prompt()
                .user(prompt)
                .call()
                .entity(ParchmentAnalysis.class);

        comments.forEach(c->c.setProcessed(true));

        commentRepository.saveAll(comments);

        return parchmentAnalysis;
    }
}