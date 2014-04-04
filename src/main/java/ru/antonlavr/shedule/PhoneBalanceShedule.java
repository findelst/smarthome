package ru.antonlavr.shedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.antonlavr.model.PhoneBalance;
import ru.antonlavr.model.settings.MobilePhones;
import ru.antonlavr.repository.PhoneBalanceRepository;
import ru.antonlavr.repository.settings.MobilePhonesRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;

@EnableScheduling
@Service
public class PhoneBalanceShedule {

    @Autowired
    PhoneBalanceRepository phoneBalanceRepository;
    @Autowired
    MobilePhonesRepository mobilePhonesRepository;

    public PhoneBalanceShedule() {}

    private List<PhoneBalance> phoneBalances;
    private PhoneBalance phoneBalanceCurrent = null;
    private List<MobilePhones> mobilePhonesList;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void getWeather() {
        mobilePhonesList = (List<MobilePhones>) mobilePhonesRepository.findAll();
        for (MobilePhones mobilePhones : mobilePhonesList) {
            phoneBalanceCurrent = downloadPhoneBalance(mobilePhones.getNumber(), mobilePhones.getPassword());
            if (phoneBalanceRepository.count() == 0) {
                phoneBalanceRepository.save(phoneBalanceCurrent);
            } else {
                if (phoneBalanceRepository.findByPhoneNumberAndBalance(phoneBalanceCurrent.getPhoneNumber(),phoneBalanceCurrent.getBalance()).size() == 0){
                    phoneBalances = phoneBalanceRepository.findByPhoneNumberAndCurrent(phoneBalanceCurrent.getPhoneNumber(), true);
                    for (PhoneBalance phoneBalance : phoneBalances){
                        phoneBalance.setCurrent(false);
                        phoneBalanceRepository.save(phoneBalance);
                    }
                    phoneBalanceRepository.save(phoneBalanceCurrent);
                }
            }
        }
    }

    public PhoneBalance downloadPhoneBalance(String userName, String password) {
        PhoneBalance phoneBalance = new PhoneBalance();
        try {
            URL url = new URL("https://volgasg.megafon.ru/ROBOTS/SC_TRAY_INFO?X_Username=" + userName + "&X_Password=" + password);
            InputStream stream = url.openStream();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stream);

            doc.getDocumentElement().normalize();

            Node root = doc.getDocumentElement();
            NodeList rootNodeList = root.getChildNodes();

            for(int i = 0; i < rootNodeList.getLength(); i++){
                if (rootNodeList.item(i).getNodeName() == "NUMBER"){
                    phoneBalance.setPhoneNumber(rootNodeList.item(i).getTextContent());
                }
                if (rootNodeList.item(i).getNodeName() == "BALANCE"){
                    phoneBalance.setBalance(new BigDecimal(rootNodeList.item(i).getTextContent()));
                    phoneBalance.setCurrent(true);
                }
                if (rootNodeList.item(i).getNodeName() == "DATE"){
                    phoneBalance.setDateCheck(new Date());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phoneBalance;
    }

}
