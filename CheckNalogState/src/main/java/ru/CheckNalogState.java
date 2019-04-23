package ru;
import unisoft.ws.FNSNDSCAWS2;
import unisoft.ws.FNSNDSCAWS2Port;
import unisoft.ws.fnsndscaws2.request.NdsRequest2;
import unisoft.ws.fnsndscaws2.request.ObjectFactory;
import unisoft.ws.fnsndscaws2.response.NdsResponse2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CheckNalogState {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        for(;;){
            System.out.println("Введите ИНН(12 цифр для ЮЛ, 10 для ФЛ): ");
            String INN = scanner.nextLine();
            String KPP = "";
            if(INN.length() != 10 && INN.length() != 12){
                System.out.println("Недопустимое количество символов ИНН.");
                continue;
            }
            if(!INN.chars().allMatch( Character::isDigit )){
                System.out.println("Недопустимые символы в ИНН.");
                continue;
            }
            if(!INNChecker.isCorrectINN(INN)){
                System.out.println("Некорректный ИНН.");
                continue;
            }
            if(INN.length() == 10){
                for(;;){
                    System.out.println("Введите КПП(9 цифр): ");
                    KPP = scanner.nextLine();
                    if(KPP.length() != 9){
                        System.out.println("Недопустимое количество символов КПП.");
                        continue;
                    }
                    if(!KPP.chars().allMatch( Character::isDigit )){
                        System.out.println("Недопустимые символы в КПП.");
                        continue;
                    }
                    break;
                }
            }

            System.out.println(getMsgByCode(Check(INN, KPP,
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))));

        }
    }

    private static String Check(String INN, String KPP, String Date){
        FNSNDSCAWS2 fnsndscaws2= new FNSNDSCAWS2();
        FNSNDSCAWS2Port fnsndscaws2Port = fnsndscaws2.getFNSNDSCAWS2Port();

        ObjectFactory objectFactory = new ObjectFactory();
        NdsRequest2.NP np = objectFactory.createNdsRequest2NP();
        np.setINN(INN);
        np.setKPP(KPP);
        np.setDT(Date);

        NdsRequest2 ndsRequest2 =  objectFactory.createNdsRequest2();
        List<NdsRequest2.NP> npRequestList = ndsRequest2.getNP();
        npRequestList.add(np);

        return fnsndscaws2Port.ndsRequest2(ndsRequest2).getNP().get(0).getState();
    }

    private static String getMsgByCode(String code){
        switch (code){
            case "0":
                return "Налогоплательщик зарегистрирован в ЕГРН и имел статус действующего в указанную дату.";
            case "1":
                return "Налогоплательщик зарегистрирован в ЕГРН, но не имел статус действующего в указанную дату.";
            case "2":
                return "Налогоплательщик зарегистрирован в ЕГРН.";
            case "3":
                return "Налогоплательщик с указанным ИНН зарегистрирован в ЕГРН, КПП не соответствует ИНН или не указан.";
            case "4":
                return "Налогоплательщик с указанным ИНН не зарегистрирован в ЕГРН.";
            default:
                return code;
        }
    }

}
