package com.fnranked.tournament.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.TimeUnit;

public class EpicAPI {

    private static LoadingCache<String, String> cachedUserNames = CacheBuilder.newBuilder()
            .maximumSize(30000L)
            .expireAfterWrite(6, TimeUnit.HOURS)
            .build(new CacheLoader<>() {

                @Override
                public String load(String member) {
                    return getEpicName(member);
                }
            });

    private static LoadingCache<String, String> cachedEpicID = CacheBuilder.newBuilder()
            .maximumSize(30000L)
            .expireAfterWrite(48, TimeUnit.HOURS)
            .build(new CacheLoader<>() {

                @Override
                public String load(String member) {
                    return getEpicID(member);
                }
            });

    public static String getEpicName(String discordId) {
        try {
            return cachedUserNames.get(discordId);
        }catch(Exception e) {
            return null;
        }
    }

    public static String getEpicID(String discordId) {
        try {
            return cachedEpicID.get(discordId);
        }catch(Exception e) {
            return null;
        }
    }

//    public static String getNameFromDataBase(String discordID) {
//        MySQL mySQL = FnrTournamentBot.getMySQL();
//        PreparedStatement ps = mySQL.getPreparedStatement("SELECT * FROM `verification` WHERE `discordID` = ?");
//        try {
//            ps.setString(1, discordID);
//            ResultSet result = ps.executeQuery();
//            while(true) {
//                try {
//                    if(!result.next())
//                        break;
//                    cachedEpicID.put(discordID, result.getString("epicID"));
//                    cachedUserNames.put(discordID, result.getString("epicDisplayName"));
//                    return result.getString("epicDisplayName");
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static String getIDFromDataBase(String discordID) {
//        MySQL mySQL = FnrTournamentBot.getMySQL();
//        PreparedStatement ps = mySQL.getPreparedStatement("SELECT * FROM `verification` WHERE `discordID` = ?");
//        try {
//            ps.setString(1, discordID);
//            ResultSet result = ps.executeQuery();
//            while(true) {
//                try {
//                    if(!result.next())
//                        break;
//                    cachedEpicID.put(discordID, result.getString("epicID"));
//                    cachedUserNames.put(discordID, result.getString("epicDisplayName"));
//                    return result.getString("epicID");
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
