package infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.valueprojects.mock_spring.model.Participante;

public class VencedorDao {
    private Connection conexao;

    public VencedorDao() {
        try {
            this.conexao = DriverManager.getConnection(
                "jdbc:mysql://localhost/mocks", "root", ""
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void salvar(Participante vencedor) {
        try {
            String sql = "INSERT INTO PARTICIPANTE (ID, NOME) VALUES (?, ?) "
                       + "ON DUPLICATE KEY UPDATE NOME = VALUES(NOME);";
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setInt(1, vencedor.getId());
            ps.setString(2, vencedor.getNome());

            ps.executeUpdate();
            ps.close();

            System.out.println("Vencedor " + vencedor.getNome() + " salvo na base de dados.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


