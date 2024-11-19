/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author pc
 */
@Entity
@Table(name = "produtos")
@NamedQueries({
    @NamedQuery(name = "Produtos.findAll", query = "SELECT p FROM Produtos p"),
    @NamedQuery(name = "Produtos.findByProdutoId", query = "SELECT p FROM Produtos p WHERE p.produtoId = :produtoId"),
    @NamedQuery(name = "Produtos.findByNome", query = "SELECT p FROM Produtos p WHERE p.nome = :nome"),
    @NamedQuery(name = "Produtos.findByEstoque", query = "SELECT p FROM Produtos p WHERE p.estoque = :estoque"),
    @NamedQuery(name = "Produtos.findByPre\u00e7o", query = "SELECT p FROM Produtos p WHERE p.pre\u00e7o = :pre\u00e7o")})
public class Produtos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "produto_id")
    private Integer produtoId;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "estoque")
    private int estoque;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "pre\u00e7o")
    private BigDecimal preço;
    @OneToMany(mappedBy = "idProduto")
    private Collection<Movimento> movimentoCollection;

    public Produtos() {
    }

    public Produtos(Integer produtoId) {
        this.produtoId = produtoId;
    }

    public Produtos(Integer produtoId, String nome, int estoque, BigDecimal preço) {
        this.produtoId = produtoId;
        this.nome = nome;
        this.estoque = estoque;
        this.preço = preço;
    }

    public Integer getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Integer produtoId) {
        this.produtoId = produtoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public BigDecimal getPreço() {
        return preço;
    }

    public void setPreço(BigDecimal preço) {
        this.preço = preço;
    }

    public Collection<Movimento> getMovimentoCollection() {
        return movimentoCollection;
    }

    public void setMovimentoCollection(Collection<Movimento> movimentoCollection) {
        this.movimentoCollection = movimentoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (produtoId != null ? produtoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Produtos)) {
            return false;
        }
        Produtos other = (Produtos) object;
        if ((this.produtoId == null && other.produtoId != null) || (this.produtoId != null && !this.produtoId.equals(other.produtoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Produtos[ produtoId=" + produtoId + " ]";
    }
    
}
