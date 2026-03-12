package com.murillo.bufonio.service;

import com.murillo.bufonio.dto.ParchmentAnalysis;
import com.murillo.bufonio.model.Comment;

import java.util.List;

public interface BufonioService {
    ParchmentAnalysis generateParchmentAnalysis(List<Comment> comments);
}
