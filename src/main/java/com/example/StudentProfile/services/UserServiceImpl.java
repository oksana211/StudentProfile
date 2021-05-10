package com.example.StudentProfile.services;

import com.example.StudentProfile.config.jwt.JwtProvider;
import com.example.StudentProfile.dto.CustomUserDetails;
import com.example.StudentProfile.dto.Statistic;
import com.example.StudentProfile.exceptions.ErrorMessages;
import com.example.StudentProfile.exceptions.ResourceNotFoundException;
import com.example.StudentProfile.models.*;
import com.example.StudentProfile.repositories.UserInfoRepository;
import com.example.StudentProfile.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private SessionServiceImpl sessionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ActivityServiceImpl activityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;


    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = findUserByLogin(username);
        return CustomUserDetails.fromUserEntityToCustomUserDetails(userEntity);
    }

    public CustomUserDetails loadUserById(String id) throws UsernameNotFoundException {
        User userEntity = getUserById(Long.valueOf(id)).get();
        return CustomUserDetails.fromUserEntityToCustomUserDetails(userEntity);
    }


    public Boolean compareId(String header, Long id){
        String token = header.substring(7);
        return jwtProvider.getIdFromToken(token).equals(id.toString());
    }

    public List<User> findAll(){
        return (List<User>) userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        User user = userRepository.findById(id).get();

        List<Activity> activities = new ArrayList<>();
        results(user, activities);

        return Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessages.NO_RESOURCE_FOUND.getErrorMessage() + id)));
    }

    public Long getInfoId(User user){
        return userRepository.findUserInfoIdByUserId(user.getId());
    }
    public UserInfo getUserInfo(User user){
        return userInfoRepository.findById(getInfoId(user)).get();
    }

    public User registerUser(User user) throws Exception {
        if(findUserByLogin(user.getLogin())==null){
            Role role = roleService.findRoleById(2L).get();
            user.setRole(role);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        else{
            throw new Exception("User with this login already exist");
        }

    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public UserInfo saveUserInfo(UserInfo user){
        return userInfoRepository.save(user);
    }

    public void deleteUser(Long id) {
        if(getUserById(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }

    public User findUserByLogin(String login){
        return userRepository.findByLogin(login);
    }

    public User findByLoginAndPassword(String login, String password) {
        User userEntity = findUserByLogin(login);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        return null;
    }

    public Statistic getStatistic(){
        List<User> users = findAll();
        List<Activity> activities = new ArrayList<>();
        for(User u: users){
            results(u, activities);
        }

        Statistic statistic = new Statistic();
        int ironRunning = 0;
        int bronzeRunning = 0;
        int silverRunning = 0;
        int goldenRunning = 0;

        int ironPullUp = 0;
        int bronzePullUp = 0;
        int silverPullUp = 0;
        int goldenPullUp = 0;

        for(Activity a: activities){
            if(a.getName().equals("running")){
                if(a.getResult().equals("Iron"))
                    ironRunning++;
                if(a.getResult().equals("Bronze"))
                    bronzeRunning++;
                if(a.getResult().equals("Silver"))
                    silverRunning++;
                if(a.getResult().equals("Golden"))
                    goldenRunning++;
            }
            if(a.getName().equals("pull up")){
                if(a.getResult().equals("Iron"))
                    ironPullUp++;
                if(a.getResult().equals("Bronze"))
                    bronzePullUp++;
                if(a.getResult().equals("Silver"))
                    silverPullUp++;
                if(a.getResult().equals("Golden"))
                    goldenPullUp++;
            }
        }
        statistic.setIronRunning(ironRunning);
        statistic.setBronzeRunning(bronzeRunning);
        statistic.setSilverRunning(silverRunning);
        statistic.setGoldenRunning(goldenRunning);

        statistic.setIronPullUp(ironPullUp);
        statistic.setBronzePullUp(bronzePullUp);
        statistic.setSilverPullUp(silverPullUp);
        statistic.setGoldenPullUp(goldenPullUp);

        return statistic;
    }


    public List<String> results(User user, List<Activity> activities){
        List<String> results = new ArrayList<>();
        if(!sessionService.findSessionByUserId(user.getId()).isEmpty()){
            List<Session> sessions = sessionService.findSessionByUserId(user.getId());
            for(Session s: sessions){
                activities.addAll(activityService.findActivityBySessionId(s.getId()));
            }
            UserInfo userInfo = getUserInfo(user);
            for(Activity a: activities){
                String result = getResult(a, userInfo.getAge(), userInfo.getSex());
                results.add(result);
                a.setResult(result);
            }
        }
        return results;

    }

    public String getResult(Activity activity, int age, String gender){
        String result = null;
        if(activity.getName().equals("running") && activity.getDistance()==30){
            Double time = activity.getTime();
            result = resultsRunning30(gender, age, time);
        }
        else if(activity.getName().equals("pull up")){
            int quantity = activity.getQuantity();
            result = resultsPullUp(gender, age, quantity);
        }
        else if(activity.getName().equals("jumping")){
            int quantity = activity.getQuantity();
            result = resultsPullUp(gender, age, quantity);
        }
        else if(activity.getName().equals("swimming")){
            int quantity = activity.getQuantity();
            result = resultsPullUp(gender, age, quantity);
        }

        return result;
    }

    public String resultsPullUp(String gender, int age, int quantity){
        String result = "";
        if((age>=6 && age<=8 && quantity < 2) || (age>=9 && age<=10 && quantity < 2)
                || (age>=11 && age<=12 && quantity < 3) || (age>=13 && age<=15 && quantity < 6)
                || (age>=16 && age<=17 && quantity < 9) || (age>=18 && age<=24 && quantity < 10)){
            result = "Iron";
        }
        else if((age>=6 && age<=8 && quantity == 2) || (age>=9 && age<=10 && quantity == 2)
                || (age>=11 && age<=12 && quantity == 3) || (age>=13 && age<=15 && quantity >= 6 && quantity < 8)
                || (age>=16 && age<=17 && quantity >= 9 && quantity < 11) || (age>=18 && age<=24 && quantity >= 10 && quantity < 12)){
            result = "Bronze";
        }
        else if((age>=6 && age<=8 && quantity == 3) || (age>=9 && age<=10 && quantity >= 3 && quantity < 5)
                || (age>=11 && age<=12 && quantity >= 4 && quantity < 7) || (age>=13 && age<=15 && quantity >= 8 && quantity < 12)
                || (age>=16 && age<=17 && quantity >= 11 && quantity < 14) || (age>=18 && age<=24 && quantity >= 12 && quantity < 15)){
            result = "Silver";
        }
        else if((age>=6 && age<=8 && quantity >= 4) || (age>=9 && age<=10 && quantity >=5 )
                || (age>=11 && age<=12 && quantity >= 7) || (age>=13 && age<=15 && quantity >= 12)
                || (age>=16 && age<=17 && quantity >= 14) || (age>=18 && age<=24 && quantity >= 15)){
            result = "Golden";
        }
        return result;
    }

    public String resultsRunning30(String gender, int age, double time){
        String result = "";
        if(gender.equals("male")){
            if((age>=6 && age<=8 && time > 6.9) || (age>=9 && age<=10 && time > 6.2)
                    || (age>=11 && age<=12 && time > 5.7) || (age>=13 && age<=15 && time > 5.3)
                    || (age>=16 && age<=17 && time > 4.9) || (age>=18 && age<=24 && time > 4.8)){
                result = "Iron";
            }
            else if((age>=6 && age<=8 && time > 6.7 && time <= 6.9) || (age>=9 && age<=10 && time > 6.0 && time <= 6.2)
                    || (age>=11 && age<=12 && time > 5.5 && time <= 5.7) || (age>=13 && age<=15 && time > 5.1 && time <= 5.3)
                    || (age>=16 && age<=17 && time > 4.7 && time <= 4.9) || (age>=18 && age<=24 && time > 4.6 && time <= 4.8)){
                result = "Bronze";
            }
            else if((age>=6 && age<=8 && time > 6.0 && time <= 6.7) || (age>=9 && age<=10 && time > 5.4 && time <= 6.0)
                    || (age>=11 && age<=12 && time > 5.1 && time <= 5.5) || (age>=13 && age<=15 && time > 4.7 && time <= 5.1)
                    || (age>=16 && age<=17 && time > 4.4 && time <= 4.7) || (age>=18 && age<=24 && time > 4.3 && time <= 4.6)){
                result = "Silver";
            }
            else if((age>=6 && age<=8 && time <= 6.0) || (age>=9 && age<=10 && time <= 5.4)
                    || (age>=11 && age<=12 && time <= 5.1) || (age>=13 && age<=15 && time <= 4.7)
                    || (age>=16 && age<=17 && time <= 4.4) || (age>=18 && age<=24 && time <= 4.3)){
                result = "Golden";
            }
        }
        else if(gender.equals("female")){
            if((age>=6 && age<=8 && time > 7.1) || (age>=9 && age<=10 && time > 6.4)
                    || (age>=11 && age<=12 && time > 6.0) || (age>=13 && age<=15 && time > 5.6)
                    || (age>=16 && age<=17 && time > 5.7) || (age>=18 && age<=24 && time > 5.9)){
                result = "Iron";
            }
            else if((age>=6 && age<=8 && time > 6.8 && time <= 7.1) || (age>=9 && age<=10 && time > 6.2 && time <= 6.4)
                    || (age>=11 && age<=12 && time > 5.8 && time <= 6.0) || (age>=13 && age<=15 && time > 5.4 && time <= 5.6)
                    || (age>=16 && age<=17 && time > 5.5 && time <= 5.7) || (age>=18 && age<=24 && time > 5.7 && time <= 5.9)){
                result = "Bronze";
            }
            else if((age>=6 && age<=8 && time > 6.2 && time <= 6.8) || (age>=9 && age<=10 && time > 5.6 && time <= 6.2)
                    || (age>=11 && age<=12 && time > 5.3 && time <= 5.8) || (age>=13 && age<=15 && time > 5.0 && time <= 5.4)
                    || (age>=16 && age<=17 && time > 5.0 && time <= 5.5) || (age>=18 && age<=24 && time > 5.7 && time <= 5.1)){
                result = "Silver";
            }
            else if((age>=6 && age<=8 && time <= 6.2) || (age>=9 && age<=10 && time <= 5.6)
                    || (age>=11 && age<=12 && time <= 5.3) || (age>=13 && age<=15 && time <= 5.0)
                    || (age>=16 && age<=17 && time <= 5.0) || (age>=18 && age<=24 && time <= 5.1)){
                result = "Golden";
            }
        }
        return result;
    }

    public static void backup2()
            throws IOException, InterruptedException, URISyntaxException {

        CodeSource codeSource = UserServiceImpl.class.getProtectionDomain().getCodeSource();
        File jarFile = new File(codeSource.getLocation().toURI().getPath());
        String jarDir = jarFile.getParentFile().getPath();

        String dbCommand = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump";
        String dbName = "profilesdb";
        String dbUser = "root";
        String dbPass = "root";

        String folderPath = jarDir + "\\backup";
        File f1 = new File(folderPath);
        f1.mkdir();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date(System.currentTimeMillis());
        String date2 = formatter.format(date).replace(":", ".");
        String savePath = "\"" + jarDir + "\\backup\\" + "backup_" + date2 + ".sql\"";

        String executeCmd = dbCommand + " -u" + dbUser
                + " -p" + dbPass + " --add-drop-table --databases "
                + dbName + " -r " + savePath;

        Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);

        int processComplete = runtimeProcess.waitFor();
        if (processComplete == 0) {
            System.out.println("Backup Complete");
        } else {
            System.out.println("Backup Failure");
        }

    }

    public static void restoreDatabase(String mysqlExe,String dbUserName, String dbPassword, String source) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String[] executeCmd = new String[]{mysqlExe, "--user=" + dbUserName, "--password=" + dbPassword, "-e", "source " + source};

        Process runtimeProcess;
        try {
            System.out.println("Processing.. "+ "STARTED " +sdf.format(new Date()));
            Date sDate = new Date();

            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = 0;
            try {
                processComplete = runtimeProcess.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Processing.. "+ "END " +sdf.format(new Date()));
            Date eDate = new Date();
            long duration =  eDate.getTime() - sDate.getTime();
            int seconds=(int) ((duration/1000)%60);
            long minutes=((duration-seconds)/1000)/60;
            System.err.println("TOTAL TIME : " + minutes +" minutes :: ");
            System.err.print(seconds +" seconds :: ");
            System.err.print(duration +" milliseconds");

            if (processComplete == 0) {
                System.out.println("Backup restored successfully with " + source);
            } else {
                System.out.println("Could not restore the backup " + source);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
