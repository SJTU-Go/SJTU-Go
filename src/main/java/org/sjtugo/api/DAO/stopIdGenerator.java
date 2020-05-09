//package org.sjtugo.api.DAO;
//
//import org.hibernate.HibernateException;
//import org.hibernate.engine.spi.SharedSessionContractImplementor;
//import org.hibernate.id.IdentityGenerator;
//
//import java.io.Serializable;
//
///**
// * @author Tony Zhou
// * 主键生成策略，检查格式是否符合stopID
// */
//public class stopIdGenerator extends IdentityGenerator {
//
//    @Override
//    public Serializable generate(SharedSessionContractImplementor s, Object obj)
//            throws HibernateException {
//        Serializable id = s.getEntityPersister(null, obj).getClassMetadata().getIdentifier(obj, s);
//        if (id != null) {
//            String idPattern;
//            int idNum;
//            try {
//                idPattern = id.toString().substring(0,3);
//                idNum = Integer.parseInt(id.toString().substring(3));
//            } catch (Exception e) {
//                throw new IdSyntaxException("busID");
//            }
//            if (!"BST".equals(idPattern) || idNum <= 0 ) {
//                throw new IdSyntaxException("busID");
//            }
//            else {
//                return id;
//            }
//        } else {
//            return super.generate(s, obj);
//        }
//    }
//}