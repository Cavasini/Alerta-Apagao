package com.alertaApagao.monitoringService.evaluation;

import com.alertaApagao.monitoringService.model.WeatherResponse;

public class RiskEvaluator {

    public static String evaluateRisk(WeatherResponse data, boolean simulateBadWeather, String cep){
        double gust;
        double precip;
        double humidity;
        double visibility;
        int conditionCode;

        if(simulateBadWeather){
            gust = 70;
            precip = 21;
            humidity = 100;
            visibility = 1;
            conditionCode = 1087;
        }else{
            gust = data.getCurrent().getGust_kph();
            precip = data.getCurrent().getPrecip_mm();
            humidity = data.getCurrent().getHumidity();
            visibility = data.getCurrent().getVis_km();
            conditionCode = data.getCurrent().getCondition().getCode();
        }

        StringBuilder reason = new StringBuilder();

        if (gust > 50) {
            reason.append("\uD83D\uDCA8 Rajadas de vento acima de 50 km/h.\n");
        }
        if (precip > 20) {
            reason.append("\uD83C\uDF27\uFE0F Chuva intensa acima de 20mm.\n");
        }
        if (humidity > 90 && precip > 10) {
            reason.append("\uD83D\uDCA7 Alta umidade combinada com chuva significativa.\n");
        }
        if (visibility < 2) {
            reason.append("\uD83C\uDF2B\uFE0F Baixa visibilidade abaixo de 2 km.\n");
        }
        if (conditionCode == 1087 || (conditionCode >= 1273 && conditionCode <= 1282)) {
            reason.append("⛈\uFE0F Tempestade elétrica detectada.\n");
        }
        if (!reason.isEmpty()) {
            return "⚠️ *Risco de apagão detectado!*\n"
                    + "📍 CEP: " + formatCep(cep) + "\n\n"
                    + "🌐 Condições identificadas:\n"
                    + reason.toString().trim();
        } else {
            return "✅ Sem risco de apagão detectado.";
        }
    }

    private static String formatCep(String cep) {
        if (cep != null && cep.length() == 8) {
            return cep.substring(0, 5) + "-" + cep.substring(5);
        }
        return cep;
    }
}
