/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.sql.Connection;
import config.DBConfig;
import entity.BlockResourceEntity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class ResourceRepository {

    public List<BlockResourceEntity> getPaginatedBlockResourceList(int page, int entries) throws SQLException {
        List<BlockResourceEntity> list = new ArrayList<>();
        Connection con = DBConfig.getConnection();
        String query = """
                       select bs.BID ,b.[name] as blockName, bs.RID,r.[name] as resourceName, bs.quantity 
                       from BlockResource as bs 
                        join BlockVin as b on bs.BID = b.BID 
                        join [Resource] as r on bs.RID = r.RID
                        order by bs.BID offset ? rows fetch next ? rows only""";

        PreparedStatement stm = con.prepareStatement(query);
        stm.setInt(1, page);
        stm.setInt(2, entries);
        ResultSet rs = stm.executeQuery();
        while (rs.next()) {
            BlockResourceEntity br = new BlockResourceEntity();
            br.setbId(rs.getInt("bid"));
            br.setrId(rs.getInt("rid"));
            br.setBlockName(rs.getString("blockName"));
            br.setResourceName(rs.getString("resourceName"));
            br.setQuantity(rs.getInt("quantity"));
            list.add(br);
        }
        return list;
    }

    public List<BlockResourceEntity> getPaginatedBlockResourceListByBlockName(int page, int entries, String blockName, boolean isRunOutOfResource) throws SQLException {
        List<BlockResourceEntity> list = new ArrayList<>();
        Connection con = DBConfig.getConnection();
        String query;
        if(isRunOutOfResource) {
             query = """
                       select bs.BID ,b.[name] as blockName, bs.RID,r.[name] as resourceName, bs.quantity 
                       from BlockResource as bs 
                        join BlockVin as b on bs.BID = b.BID 
                        join [Resource] as r on bs.RID = r.RID
                        where b.[name] = ? and bs.quantity = 0
                        order by bs.BID offset ? rows fetch next ? rows only""";
        } else {
             query = """
                       select bs.BID ,b.[name] as blockName, bs.RID,r.[name] as resourceName, bs.quantity 
                       from BlockResource as bs 
                        join BlockVin as b on bs.BID = b.BID 
                        join [Resource] as r on bs.RID = r.RID
                        where b.[name] = ? 
                        order by bs.BID offset ? rows fetch next ? rows only""";
        }
        PreparedStatement stm = con.prepareStatement(query);
        stm.setInt(2, page);
        stm.setString(1, blockName);
        stm.setInt(3, entries);
        ResultSet rs = stm.executeQuery();
        while (rs.next()) {
            BlockResourceEntity br = new BlockResourceEntity();
            br.setbId(rs.getInt("bid"));
            br.setrId(rs.getInt("rid"));
            br.setBlockName(rs.getString("blockName"));
            br.setResourceName(rs.getString("resourceName"));
            br.setQuantity(rs.getInt("quantity"));
            list.add(br);
        }
        return list;
    }
    
    public List<BlockResourceEntity> getPaginatedBlockResourceListBySearchedBlockName(int page, int entries, String searchedName, boolean isRunOutOfResource) throws SQLException {
        List<BlockResourceEntity> list = new ArrayList<>();
        Connection con = DBConfig.getConnection();
        String query;
        if(isRunOutOfResource) {
            query = """
                       select bs.BID ,b.[name] as blockName, bs.RID,r.[name] as resourceName, bs.quantity 
                       from BlockResource as bs 
                        join BlockVin as b on bs.BID = b.BID 
                        join [Resource] as r on bs.RID = r.RID
                        where b.[name] like ? and bs.quantity = 0
                        order by bs.BID offset ? rows fetch next ? rows only""";
        } else {
            query = """
                       select bs.BID ,b.[name] as blockName, bs.RID,r.[name] as resourceName, bs.quantity 
                       from BlockResource as bs 
                        join BlockVin as b on bs.BID = b.BID 
                        join [Resource] as r on bs.RID = r.RID
                        where b.[name] like ?
                        order by bs.BID offset ? rows fetch next ? rows only""";
        }
        PreparedStatement stm = con.prepareStatement(query);
        stm.setInt(2, page);
        stm.setString(1, "%"+ searchedName + "%");
        stm.setInt(3, entries);
        ResultSet rs = stm.executeQuery();
        while (rs.next()) {
            BlockResourceEntity br = new BlockResourceEntity();
            br.setbId(rs.getInt("bid"));
            br.setrId(rs.getInt("rid"));
            br.setBlockName(rs.getString("blockName"));
            br.setResourceName(rs.getString("resourceName"));
            br.setQuantity(rs.getInt("quantity"));
            list.add(br);
        }
        return list;
    }
   
    
    public boolean updateResource(BlockResourceEntity br) throws SQLException {
        boolean check = false;
        Connection con = DBConfig.getConnection();
        String query = """
                       update BlockResource 
                       set quantity = ?
                       where BlockResource.BID = ? and BlockResource.RID = ?""";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setInt(1, br.getQuantity());
        stm.setInt(2, br.getbId());
        stm.setInt(3, br.getrId());
        int count = stm.executeUpdate();
        if(count != 0) {
            check = true;
        }
        return check;
    }

    public static void main(String[] args) throws SQLException {
        BlockResourceEntity br = new BlockResourceEntity(1, "Toa cc", 17, "cc", 20);
        System.out.println(new ResourceRepository().getPaginatedBlockResourceListBySearchedBlockName(0, 10, "S102", true).size());
    }
}