package com.ecommerce.complaints.service.formatter;

import com.ecommerce.complaints.model.generate.ComplaintAnalysisVTO;

import org.springframework.stereotype.Component;

@Component
public class AnalysisFormatter {


    public String formatAnalysis(ComplaintAnalysisVTO analysis) {
        StringBuilder text = new StringBuilder();
        appendCategory(text, analysis);
        appendSentiment(text, analysis);
        appendPriority(text, analysis);
        appendKeywords(text, analysis);
        appendSummary(text, analysis);
        appendTimestamp(text, analysis);
        return text.toString();
    }


    private void appendCategory(StringBuilder text, ComplaintAnalysisVTO analysis) {
        text.append("Category: ").append(analysis.getCategory());
        if (analysis.getCategoryConfidence() != null) {
            text.append(" (confidence: ")
                    .append(formatPercentage(analysis.getCategoryConfidence()))
                    .append(")");
        }
        text.append("\n\n");
    }

    private void appendSentiment(StringBuilder text, ComplaintAnalysisVTO analysis) {
        text.append("Sentiment: ").append(analysis.getSentiment());

        if (analysis.getSentimentScore() != null) {
            text.append(" (score: ")
                    .append(String.format("%.2f", analysis.getSentimentScore()))
                    .append(")");
        }
        text.append("\n\n");
    }

    private void appendPriority(StringBuilder text, ComplaintAnalysisVTO analysis) {
        text.append("Priority: ").append(analysis.getPriority());

        if (analysis.getUrgencyScore() != null) {
            text.append(" (urgency: ")
                    .append(formatPercentage(analysis.getUrgencyScore()))
                    .append(")");
        }
        text.append("\n");
        text.append("\n\n");
    }

    private void appendKeywords(StringBuilder text, ComplaintAnalysisVTO analysis) {
        if (analysis.getKeywords() != null && !analysis.getKeywords().isEmpty()) {
            text.append("Keywords:\n");
            analysis.getKeywords().forEach(keyword ->
                    text.append("   • ").append(keyword).append("\n")
            );
            text.append("\n");
        }
    }

    private void appendSummary(StringBuilder text, ComplaintAnalysisVTO analysis) {
        if (analysis.getSummary() != null && !analysis.getSummary().isEmpty()) {
            text.append("Summary:\n");
            text.append(analysis.getSummary()).append("\n\n");
        }
    }

    private void appendTimestamp(StringBuilder text, ComplaintAnalysisVTO analysis) {
        text.append("Analyzed at: ").append(analysis.getAnalyzedAt());
    }

    private String formatPercentage(Double value) {
        return String.format("%.1f%%", value * 100);
    }
}