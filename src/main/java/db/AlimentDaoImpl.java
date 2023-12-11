package db;

import beans.Aliment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class AlimentDaoImpl extends AlimentDao {

    public AlimentDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean create(Aliment obj) throws DaoException {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                connection.setAutoCommit(true);
                try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO aliments VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")) {
                    preparedStatement.setString(1, obj.getNom());
                    preparedStatement.setDouble(2, obj.getCalories());
                    preparedStatement.setString(3, obj.getUnite());
                    preparedStatement.setDouble(4, obj.getGlucides());
                    preparedStatement.setDouble(5, obj.getSucre());
                    preparedStatement.setDouble(6, obj.getFibres());
                    preparedStatement.setDouble(7, obj.getMatieresGrasses());
                    preparedStatement.setDouble(8, obj.getMatieresGrassesSatures());
                    preparedStatement.setDouble(9, obj.getMatieresGrassesMonoInsaturees());
                    preparedStatement.setDouble(10, obj.getMatieresGrassesPolyInsaturees());
                    preparedStatement.setDouble(11, obj.getMatieresGrassesTrans());
                    preparedStatement.setDouble(12, obj.getProteines());
                    preparedStatement.setDouble(13, obj.getSel());
                    preparedStatement.setDouble(14, obj.getCholesterol());
                    preparedStatement.setDouble(15, obj.getVitamineA());
                    preparedStatement.setDouble(16, obj.getVitamineB1());
                    preparedStatement.setDouble(17, obj.getVitamineB2());
                    preparedStatement.setDouble(18, obj.getVitamineB3());
                    preparedStatement.setDouble(19, obj.getVitamineB5());
                    preparedStatement.setDouble(20, obj.getVitamineB6());
                    preparedStatement.setDouble(21, obj.getVitamineB8());
                    preparedStatement.setDouble(22, obj.getVitamineB11());
                    preparedStatement.setDouble(23, obj.getVitamineB12());
                    preparedStatement.setDouble(24, obj.getVitamineC());
                    preparedStatement.setDouble(25, obj.getVitamineD());
                    preparedStatement.setDouble(26, obj.getVitamineE());
                    preparedStatement.setDouble(27, obj.getVitamineK());
                    preparedStatement.setDouble(28, obj.getArs());
                    preparedStatement.setDouble(29, obj.getB());
                    preparedStatement.setDouble(30, obj.getCa());
                    preparedStatement.setDouble(31, obj.getCl());
                    preparedStatement.setDouble(32, obj.getCholine());
                    preparedStatement.setDouble(33, obj.getCr());
                    preparedStatement.setDouble(34, obj.getCo());
                    preparedStatement.setDouble(35, obj.getCu());
                    preparedStatement.setDouble(36, obj.getFe());
                    preparedStatement.setDouble(37, obj.getF());
                    preparedStatement.setDouble(38, obj.getI());
                    preparedStatement.setDouble(39, obj.getMg());
                    preparedStatement.setDouble(40, obj.getMn());
                    preparedStatement.setDouble(41, obj.getMo());
                    preparedStatement.setDouble(42, obj.getP());
                    preparedStatement.setDouble(43, obj.getK());
                    preparedStatement.setDouble(44, obj.getRb());
                    preparedStatement.setDouble(45, obj.getSiO());
                    preparedStatement.setDouble(46, obj.getS());
                    preparedStatement.setDouble(47, obj.getSe());
                    preparedStatement.setDouble(48, obj.getV());
                    preparedStatement.setDouble(49, obj.getSn());
                    preparedStatement.setDouble(50, obj.getZn());

                    int nbRows = preparedStatement.executeUpdate();
                    return nbRows > 0;
                }
            } else {
                throw new DaoException("Impossible de se connecter à la base de données");
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Aliment get(String id) throws DaoException {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM aliments WHERE nomAliment = ?;")) {
                    preparedStatement.setString(1, id);
                    try (ResultSet result = preparedStatement.executeQuery()) {
                        if (result.next()) {
                            Aliment aliment = new Aliment();
                            aliment.setNom(result.getString("nomAliment"));
                            aliment.setCalories(result.getDouble("calories"));
                            aliment.setUnite(result.getString("unite"));
                            aliment.setGlucides(result.getDouble("glucides"));
                            aliment.setSucre(result.getDouble("sucre"));
                            aliment.setFibres(result.getDouble("fibres"));
                            aliment.setMatieresGrasses(result.getDouble("Mat_Gras"));
                            aliment.setMatieresGrassesSatures(result.getDouble("MG_S"));
                            aliment.setMatieresGrassesMonoInsaturees(result.getDouble("MG_MI"));
                            aliment.setMatieresGrassesPolyInsaturees(result.getDouble("MG_PI"));
                            aliment.setMatieresGrassesTrans(result.getDouble("MG_T"));
                            aliment.setProteines(result.getDouble("proteines"));
                            aliment.setSel(result.getDouble("sel"));
                            aliment.setCholesterol(result.getDouble("cholesterol"));
                            aliment.setVitamineA(result.getDouble("vit_A"));
                            aliment.setVitamineB1(result.getDouble("vit_B1"));
                            aliment.setVitamineB2(result.getDouble("vit_B2"));
                            aliment.setVitamineB3(result.getDouble("vit_B3"));
                            aliment.setVitamineB5(result.getDouble("vit_B5"));
                            aliment.setVitamineB6(result.getDouble("vit_B6"));
                            aliment.setVitamineB8(result.getDouble("vit_B8"));
                            aliment.setVitamineB11(result.getDouble("vit_B11"));
                            aliment.setVitamineB12(result.getDouble("vit_B12"));
                            aliment.setVitamineC(result.getDouble("vit_C"));
                            aliment.setVitamineD(result.getDouble("vit_D"));
                            aliment.setVitamineE(result.getDouble("vit_E"));
                            aliment.setVitamineK(result.getDouble("vit_K"));
                            aliment.setArs(result.getDouble("Ars"));
                            aliment.setB(result.getDouble("B"));
                            aliment.setCa(result.getDouble("Ca"));
                            aliment.setCl(result.getDouble("Cl"));
                            aliment.setCholine(result.getDouble("choline"));
                            aliment.setCr(result.getDouble("Cr"));
                            aliment.setCo(result.getDouble("Co"));
                            aliment.setCu(result.getDouble("Cu"));
                            aliment.setFe(result.getDouble("Fe"));
                            aliment.setF(result.getDouble("F"));
                            aliment.setI(result.getDouble("I"));
                            aliment.setMg(result.getDouble("Mg"));
                            aliment.setMn(result.getDouble("Mn"));
                            aliment.setMo(result.getDouble("Mo"));
                            aliment.setP(result.getDouble("P"));
                            aliment.setK(result.getDouble("K"));
                            aliment.setRb(result.getDouble("Rb"));
                            aliment.setSiO(result.getDouble("SiO"));
                            aliment.setS(result.getDouble("S"));
                            aliment.setSe(result.getDouble("Se"));
                            aliment.setV(result.getDouble("V"));
                            aliment.setSn(result.getDouble("Sn"));
                            aliment.setZn(result.getDouble("Zn"));
                            return aliment;
                        } else {
                            return null;
                        }
                    }

                }
            } else {
                throw new DaoException("Impossible de se connecter à la base de données");
            }
        } catch (SQLException e) {
            throw new DaoException("Impossible de communiquer avec la base de données");
        }
    }

    @Override
    public boolean update(Aliment obj, Aliment oldObj) throws DaoException {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                connection.setAutoCommit(true);
                try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE aliments SET nomAliment = ?, calories = ?, unite = ?, glucides = ?, sucre = ?, fibres = ?, Mat_Gras = ?, MG_S = ?, MG_MI = ?, MG_PI = ?, MG_T = ?, proteines = ?, sel = ?, cholesterol = ?, vit_A = ?, vit_B1 = ?, vit_B2 = ?, vit_B3 = ?, vit_B5 = ?, vit_B6 = ?, vit_B8 = ?, vit_B11 = ?, vit_B12 = ?, vit_C = ?, vit_D = ?, vit_E = ?, vit_K = ?, Ars = ?, B = ?, Ca = ?, Cl = ?, choline = ?, Cr = ?, Co = ?, Cu = ?, Fe = ?, F = ?, I = ?, Mg = ?, Mn = ?, Mo = ?, P = ?, K = ?, Rb = ?, SiO = ?, S = ?, Se = ?, V = ?, Sn = ?, Zn = ? WHERE nomAliment = ?")) {
                    preparedStatement.setString(1, obj.getNom());
                    preparedStatement.setDouble(2, obj.getCalories());
                    preparedStatement.setString(3, obj.getUnite());
                    preparedStatement.setDouble(4, obj.getGlucides());
                    preparedStatement.setDouble(5, obj.getSucre());
                    preparedStatement.setDouble(6, obj.getFibres());
                    preparedStatement.setDouble(7, obj.getMatieresGrasses());
                    preparedStatement.setDouble(8, obj.getMatieresGrassesSatures());
                    preparedStatement.setDouble(9, obj.getMatieresGrassesMonoInsaturees());
                    preparedStatement.setDouble(10, obj.getMatieresGrassesPolyInsaturees());
                    preparedStatement.setDouble(11, obj.getMatieresGrassesTrans());
                    preparedStatement.setDouble(12, obj.getProteines());
                    preparedStatement.setDouble(13, obj.getSel());
                    preparedStatement.setDouble(14, obj.getCholesterol());
                    preparedStatement.setDouble(15, obj.getVitamineA());
                    preparedStatement.setDouble(16, obj.getVitamineB1());
                    preparedStatement.setDouble(17, obj.getVitamineB2());
                    preparedStatement.setDouble(18, obj.getVitamineB3());
                    preparedStatement.setDouble(19, obj.getVitamineB5());
                    preparedStatement.setDouble(20, obj.getVitamineB6());
                    preparedStatement.setDouble(21, obj.getVitamineB8());
                    preparedStatement.setDouble(22, obj.getVitamineB11());
                    preparedStatement.setDouble(23, obj.getVitamineB12());
                    preparedStatement.setDouble(24, obj.getVitamineC());
                    preparedStatement.setDouble(25, obj.getVitamineD());
                    preparedStatement.setDouble(26, obj.getVitamineE());
                    preparedStatement.setDouble(27, obj.getVitamineK());
                    preparedStatement.setDouble(28, obj.getArs());
                    preparedStatement.setDouble(29, obj.getB());
                    preparedStatement.setDouble(30, obj.getCa());
                    preparedStatement.setDouble(31, obj.getCl());
                    preparedStatement.setDouble(32, obj.getCholine());
                    preparedStatement.setDouble(33, obj.getCr());
                    preparedStatement.setDouble(34, obj.getCo());
                    preparedStatement.setDouble(35, obj.getCu());
                    preparedStatement.setDouble(36, obj.getFe());
                    preparedStatement.setDouble(37, obj.getF());
                    preparedStatement.setDouble(38, obj.getI());
                    preparedStatement.setDouble(39, obj.getMg());
                    preparedStatement.setDouble(40, obj.getMn());
                    preparedStatement.setDouble(41, obj.getMo());
                    preparedStatement.setDouble(42, obj.getP());
                    preparedStatement.setDouble(43, obj.getK());
                    preparedStatement.setDouble(44, obj.getRb());
                    preparedStatement.setDouble(45, obj.getSiO());
                    preparedStatement.setDouble(46, obj.getS());
                    preparedStatement.setDouble(47, obj.getSe());
                    preparedStatement.setDouble(48, obj.getV());
                    preparedStatement.setDouble(49, obj.getSn());
                    preparedStatement.setDouble(50, obj.getZn());
                    preparedStatement.setString(51, Objects.requireNonNullElse(oldObj, obj).getNom());
                    int nbRows = preparedStatement.executeUpdate();
                    return nbRows > 0;
                }
            } else {
                throw new DaoException("Impossible de se connecter à la base de données");
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(Aliment obj) throws DaoException {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                connection.setAutoCommit(true);
                try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM aliments WHERE nomAliment = ?;")) {
                    preparedStatement.setString(1, obj.getNom());
                    int nbRows = preparedStatement.executeUpdate();
                    return nbRows > 0;
                }
            } else {
                throw new DaoException("Impossible de se connecter à la base de données");
            }
        } catch (SQLException e) {
            return false;
        }
    }
}
